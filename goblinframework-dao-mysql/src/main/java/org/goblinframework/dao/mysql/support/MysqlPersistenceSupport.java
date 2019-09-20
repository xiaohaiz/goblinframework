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

abstract public class MysqlPersistenceSupport<E, ID> extends MysqlPrimaryKeySupport<E, ID> {

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

  @Nullable
  public E directLoad(@NotNull final MysqlConnection connection,
                      @Nullable final ID id) {
    if (id == null) return null;
    return directLoads(connection, Collections.singleton(id)).get(id);
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

  public void __insert(@NotNull E entity) {
    __inserts(Collections.singleton(entity));
  }

  public void __inserts(@NotNull Collection<E> entities) {
    if (entities.isEmpty()) return;
    long millis = System.currentTimeMillis();
    for (E entity : entities) {
      generateEntityId(entity);
      requireEntityId(entity);
      touchCreateTime(entity, millis);
      touchUpdateTime(entity, millis);
      initializeRevision(entity);
    }
    if (entities.size() > 1 && isNestedTransactionEnabled()) {
      client.getMasterTransactionTemplate().execute(new TransactionCallbackWithoutResult() {
        @Override
        protected void doInTransactionWithoutResult(TransactionStatus status) {
          executeInserts(entities);
        }
      });
    } else {
      executeInserts(entities);
    }
  }

  private void executeInserts(final Collection<E> entities) {
    groupEntities(entities).forEach((tableName, list) -> {
      list.forEach(e -> executeInsert(e, tableName));
    });
  }

  private void executeInsert(final E entity, final String tableName) {
    MysqlInsertOperation insertOperation = new MysqlInsertOperation(entityMapping, entity, tableName);
    String sql = insertOperation.generateSQL();
    PreparedStatementCreatorFactory factory = insertOperation.newPreparedStatementCreatorFactory(sql);
    if (generator == Id.Generator.AUTO_INC && !insertOperation.isIncludeId()) {
      factory.setReturnGeneratedKeys(true);
      PreparedStatementCreator creator = factory.newPreparedStatementCreator(insertOperation.toParams());
      GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
      JdbcTemplate jdbcTemplate = client.getMasterJdbcTemplate();
      jdbcTemplate.update(creator, keyHolder);

      ConversionService conversionService = ConversionService.INSTANCE;
      Object id = conversionService.convert(keyHolder.getKey(), entityMapping.idClass);
      entityMapping.idField.setValue(entity, id);
    } else {
      PreparedStatementCreator creator = factory.newPreparedStatementCreator(insertOperation.toParams());
      JdbcTemplate jdbcTemplate = client.getMasterJdbcTemplate();
      jdbcTemplate.update(creator);
    }
  }

  private long executeCount(final Query query, final String tableName) {
    TranslatedCriteria tc = queryTranslator.translateCount(query, tableName);
    NamedParameterJdbcTemplate jdbcTemplate = client.getMasterNamedParameterJdbcTemplate();
    return jdbcTemplate.query(tc.sql, tc.parameterSource,
        (resultSet, i) -> resultSet.getLong(1)).iterator().next();
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
