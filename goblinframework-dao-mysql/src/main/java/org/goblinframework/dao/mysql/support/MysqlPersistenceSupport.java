package org.goblinframework.dao.mysql.support;

import org.goblinframework.api.annotation.Id;
import org.goblinframework.core.conversion.ConversionService;
import org.goblinframework.core.util.MapUtils;
import org.goblinframework.dao.core.cql.Criteria;
import org.goblinframework.dao.core.cql.Query;
import org.goblinframework.dao.mysql.client.MysqlConnection;
import org.goblinframework.dao.mysql.cql.*;
import org.goblinframework.dao.mysql.mapping.MysqlEntityRowMapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

abstract public class MysqlPersistenceSupport<E, ID> extends MysqlListenerSupport<E, ID> {

  protected final RowMapper<E> entityRowMapper;
  protected final MysqlCriteriaTranslator criteriaTranslator;
  protected final MysqlQueryTranslator queryTranslator;
  protected final MysqlUpdateTranslator updateTranslator;

  protected MysqlPersistenceSupport() {
    entityRowMapper = new MysqlEntityRowMapper<>(entityMapping);
    criteriaTranslator = MysqlCriteriaTranslator.INSTANCE;
    queryTranslator = new MysqlQueryTranslator(entityMapping);
    updateTranslator = MysqlUpdateTranslator.INSTANCE;
  }

  public void directInsert(@NotNull E entity) {
    MysqlConnection connection = client.getMasterConnection();
    directInsert(connection, entity);
  }

  public void directInsert(@NotNull MysqlConnection connection,
                           @NotNull E entity) {
    directInserts(connection, Collections.singleton(entity));
  }

  public void directInserts(@Nullable final Collection<E> entities) {
    MysqlConnection connection = client.getMasterConnection();
    directInserts(connection, entities);
  }

  public void directInserts(@NotNull MysqlConnection connection,
                            @Nullable final Collection<E> entities) {
    if (entities == null || entities.isEmpty()) return;

    for (E entity : entities) {
      for (BeforeInsertListener<E> listener : beforeInsertListeners) {
        listener.beforeInsert(entity);
      }
    }
    long millis = System.currentTimeMillis();
    for (E entity : entities) {
      generateEntityId(entity);
      requireEntityId(entity);
      touchCreateTime(entity, millis);
      touchUpdateTime(entity, millis);
      initializeRevision(entity);
    }
    if (entities.size() > 1) {
      connection.executeTransactionWithoutResult(new TransactionCallbackWithoutResult() {
        @Override
        protected void doInTransactionWithoutResult(TransactionStatus status) {
          groupEntities(entities).forEach((tableName, list) -> {
            list.forEach(e -> executeInsert(connection, e, tableName));
          });
        }
      });
    } else {
      E entity = entities.iterator().next();
      String tableName = getEntityTableName(entity);
      executeInsert(connection, entity, tableName);
    }
  }

  @Nullable
  public E directLoad(@Nullable final ID id) {
    MysqlConnection connection = client.getMasterConnection();
    return directLoad(connection, id);
  }

  @Nullable
  public E directLoad(@NotNull final MysqlConnection connection,
                      @Nullable final ID id) {
    if (id == null) return null;
    return directLoads(connection, Collections.singleton(id)).get(id);
  }

  @NotNull
  public Map<ID, E> directLoads(@Nullable final Collection<ID> ids) {
    MysqlConnection connection = client.getMasterConnection();
    return directLoads(connection, ids);
  }

  @NotNull
  public Map<ID, E> directLoads(@NotNull final MysqlConnection connection,
                                @Nullable final Collection<ID> ids) {
    if (ids == null || ids.isEmpty()) return Collections.emptyMap();
    List<E> entities = new LinkedList<>();
    groupIds(ids).forEach((tableName, idList) -> {
      assert !idList.isEmpty();
      Criteria criteria;
      if (idList.size() == 1) {
        ID id = idList.iterator().next();
        criteria = Criteria.where(entityMapping.getIdFieldName()).is(id);
      } else {
        criteria = Criteria.where(entityMapping.getIdFieldName()).in(idList);
      }
      Query query = Query.query(criteria);
      entities.addAll(executeQuery(connection, query, tableName));
    });
    Map<ID, E> map = entities.stream().collect(Collectors.toMap(this::getEntityId, Function.identity()));
    return MapUtils.resort(map, ids);
  }

  private void executeInsert(@NotNull final MysqlConnection connection,
                             @NotNull final E entity,
                             @NotNull final String tableName) {
    MysqlInsertOperation insertOperation = new MysqlInsertOperation(entityMapping, entity, tableName);
    String sql = insertOperation.generateSQL();
    PreparedStatementCreatorFactory factory = insertOperation.newPreparedStatementCreatorFactory(sql);
    if (generator == Id.Generator.AUTO_INC && !insertOperation.isIncludeId()) {
      factory.setReturnGeneratedKeys(true);
      PreparedStatementCreator creator = factory.newPreparedStatementCreator(insertOperation.toParams());
      GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
      JdbcTemplate jdbcTemplate = connection.getJdbcTemplate();
      jdbcTemplate.update(creator, keyHolder);

      ConversionService conversionService = ConversionService.INSTANCE;
      Object id = conversionService.convert(keyHolder.getKey(), entityMapping.idClass);
      entityMapping.idField.setValue(entity, id);
    } else {
      PreparedStatementCreator creator = factory.newPreparedStatementCreator(insertOperation.toParams());
      JdbcTemplate jdbcTemplate = connection.getJdbcTemplate();
      jdbcTemplate.update(creator);
    }
  }

  @NotNull
  private List<E> executeQuery(@NotNull final MysqlConnection connection,
                               @NotNull final Query query,
                               @NotNull final String tableName) {
    TranslatedCriteria tc = queryTranslator.translateQuery(query, tableName);
    NamedParameterJdbcTemplate jdbcTemplate = connection.getNamedParameterJdbcTemplate();
    return jdbcTemplate.query(tc.sql, tc.parameterSource, entityRowMapper);
  }
}
