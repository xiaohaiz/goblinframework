package org.goblinframework.dao.mysql.support;

import org.apache.commons.lang3.mutable.MutableObject;
import org.goblinframework.api.annotation.Id;
import org.goblinframework.core.conversion.ConversionService;
import org.goblinframework.core.util.MapUtils;
import org.goblinframework.dao.core.cql.Criteria;
import org.goblinframework.dao.core.cql.Query;
import org.goblinframework.dao.core.mapping.field.EntityRevisionField;
import org.goblinframework.dao.mysql.client.MysqlConnection;
import org.goblinframework.dao.mysql.cql.*;
import org.goblinframework.dao.mysql.mapping.MysqlEntityRowMapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
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

  public void insert(@NotNull E entity) {
    directInsert(getMasterConnection(), entity);
  }

  public void inserts(@Nullable final Collection<E> entities) {
    directInserts(getMasterConnection(), entities);
  }

  @Nullable
  public E load(@Nullable final ID id) {
    return directLoad(getMasterConnection(), id);
  }

  @NotNull
  public Map<ID, E> loads(@Nullable final Collection<ID> ids) {
    return directLoads(getMasterConnection(), ids);
  }

  public boolean exists(@Nullable ID id) {
    return directExists(getMasterConnection(), id);
  }

  public boolean replace(@Nullable E entity) {
    return directReplace(getMasterConnection(), entity);
  }

  // ==========================================================================
  // Direct database access methods
  // ==========================================================================

  final public void directInsert(@NotNull MysqlConnection connection,
                                 @NotNull E entity) {
    directInserts(connection, Collections.singleton(entity));
  }

  final public void directInserts(@NotNull MysqlConnection connection,
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
  final public E directLoad(@NotNull final MysqlConnection connection,
                            @Nullable final ID id) {
    if (id == null) return null;
    return directLoads(connection, Collections.singleton(id)).get(id);
  }

  @NotNull
  final public Map<ID, E> directLoads(@NotNull final MysqlConnection connection,
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

  final public boolean directExists(@NotNull final MysqlConnection connection,
                                    @Nullable ID id) {
    if (id == null) return false;
    Criteria criteria = Criteria.where(entityMapping.getIdFieldName()).is(id);
    Query query = Query.query(criteria);
    String tableName = getIdTableName(id);
    return executeCount(connection, query, tableName) > 0;
  }

  final public boolean directReplace(@NotNull MysqlConnection connection,
                                     @Nullable E entity) {
    if (entity == null) return false;
    ID id = getEntityId(entity);
    if (id == null) {
      logger.warn("ID must not be null when executing replace operation.");
      return false;
    }
    long millis = System.currentTimeMillis();
    touchUpdateTime(entity, millis);
    Criteria criteria = Criteria.where(entityMapping.idField.getName()).is(id);
    EntityRevisionField revisionField = entityMapping.revisionField;
    if (revisionField != null) {
      // revision specified, use it for optimistic concurrency checks
      Object revision = revisionField.getValue(entity);
      if (revision != null) {
        criteria = criteria.and(revisionField.getName()).is(revision);
      }
    }
    String tableName = getIdTableName(id);
    TranslatedCriteria tc1 = criteriaTranslator.translate(criteria);
    MysqlUpdateOperation updateOperation = new MysqlUpdateOperation(entityMapping, entity, tableName);
    TranslatedCriteria tc2 = updateOperation.generateSQL();
    if (tc2 == null) {
      logger.warn("Nothing found when executing replace operation.");
      return false;
    }

    String sql = tc2.sql + tc1.sql;
    MapSqlParameterSource parameterSource = new MapSqlParameterSource();
    parameterSource.addValues(tc2.parameterSource.getValues());
    parameterSource.addValues(tc1.parameterSource.getValues());
    NamedParameterJdbcTemplate jdbcTemplate = connection.getNamedParameterJdbcTemplate();
    int rows = jdbcTemplate.update(sql, parameterSource);
    return rows > 0;
  }


  // ==========================================================================
  // Helper methods
  // ==========================================================================

  protected void executeInsert(@NotNull final MysqlConnection connection,
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

  protected long executeDelete(@NotNull MysqlConnection connection,
                               @NotNull final Criteria criteria,
                               @NotNull final String tableName) {
    TranslatedCriteria tc = criteriaTranslator.translate(criteria);
    MapSqlParameterSource source = new MapSqlParameterSource();
    source.addValues(tc.parameterSource.getValues());
    String s = "DELETE FROM `%s`%s";
    s = String.format(s, tableName, tc.sql);
    MutableObject<String> sql = new MutableObject<>(s);
    NamedParameterJdbcTemplate jdbcTemplate = connection.getNamedParameterJdbcTemplate();
    return jdbcTemplate.update(sql.getValue(), source);
  }

  protected long executeCount(@NotNull final MysqlConnection connection,
                              @NotNull final Query query,
                              @NotNull final String tableName) {
    TranslatedCriteria tc = queryTranslator.translateCount(query, tableName);
    NamedParameterJdbcTemplate jdbcTemplate = connection.getNamedParameterJdbcTemplate();
    return jdbcTemplate.query(tc.sql, tc.parameterSource,
        (resultSet, i) -> resultSet.getLong(1)).iterator().next();
  }

  @NotNull
  protected List<E> executeQuery(@NotNull final MysqlConnection connection,
                                 @NotNull final Query query,
                                 @NotNull final String tableName) {
    TranslatedCriteria tc = queryTranslator.translateQuery(query, tableName);
    NamedParameterJdbcTemplate jdbcTemplate = connection.getNamedParameterJdbcTemplate();
    return jdbcTemplate.query(tc.sql, tc.parameterSource, entityRowMapper);
  }
}
