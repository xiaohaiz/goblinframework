package org.goblinframework.database.mysql.support;

import org.apache.commons.lang3.mutable.MutableObject;
import org.goblinframework.api.dao.Id;
import org.goblinframework.core.conversion.ConversionService;
import org.goblinframework.core.util.MapUtils;
import org.goblinframework.core.util.StringUtils;
import org.goblinframework.dao.core.persistence.BeforeInsertListener;
import org.goblinframework.database.core.eql.Criteria;
import org.goblinframework.database.core.eql.NativeSQL;
import org.goblinframework.database.core.eql.Query;
import org.goblinframework.database.core.eql.Update;
import org.goblinframework.database.core.mapping.EntityRevisionField;
import org.goblinframework.database.mysql.client.MysqlConnection;
import org.goblinframework.database.mysql.eql.*;
import org.goblinframework.database.mysql.mapping.MysqlEntityRowMapper;
import org.goblinframework.database.mysql.persistence.GoblinPersistenceException;
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
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
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

  public void insert(@NotNull E entity) {
    __insert(entity);
  }

  public void inserts(@Nullable final Collection<E> entities) {
    __inserts(entities);
  }

  @Nullable
  public E load(@Nullable final ID id) {
    return __load(getMasterConnection(), id);
  }

  @NotNull
  public Map<ID, E> loads(@Nullable final Collection<ID> ids) {
    return __loads(getMasterConnection(), ids);
  }

  public boolean exists(@Nullable ID id) {
    return __exists(getMasterConnection(), id);
  }

  public boolean replace(@Nullable E entity) {
    return __replace(entity);
  }

  public boolean upsert(@Nullable E entity) {
    return __upsert(entity);
  }

  public boolean delete(@Nullable ID id) {
    return __delete(id);
  }

  public long deletes(@Nullable Collection<ID> ids) {
    return __deletes(ids);
  }

  // ==========================================================================
  // Direct database access methods
  // ==========================================================================

  final public void __insert(@NotNull E entity) {
    __inserts(Collections.singleton(entity));
  }

  final public void __inserts(@Nullable final Collection<E> entities) {
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
      getMasterConnection().executeTransactionWithoutResult(new TransactionCallbackWithoutResult() {
        @Override
        protected void doInTransactionWithoutResult(TransactionStatus status) {
          groupEntities(entities).forEach((tableName, list) -> {
            list.forEach(e -> __executeInsert(e, tableName));
          });
        }
      });
    } else {
      E entity = entities.iterator().next();
      String tableName = getEntityTableName(entity);
      __executeInsert(entity, tableName);
    }
  }

  @Nullable
  final public E __load(@NotNull final MysqlConnection connection,
                        @Nullable final ID id) {
    if (id == null) return null;
    return __loads(connection, Collections.singleton(id)).get(id);
  }

  @NotNull
  final public Map<ID, E> __loads(@NotNull final MysqlConnection connection,
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
      entities.addAll(__executeQuery(connection, query, tableName));
    });
    Map<ID, E> map = entities.stream().collect(Collectors.toMap(this::getEntityId, Function.identity()));
    return MapUtils.resort(map, ids);
  }

  final public boolean __exists(@NotNull final MysqlConnection connection,
                                @Nullable ID id) {
    if (id == null) return false;
    Criteria criteria = Criteria.where(entityMapping.getIdFieldName()).is(id);
    Query query = Query.query(criteria);
    String tableName = getIdTableName(id);
    return __executeCount(connection, query, tableName) > 0;
  }

  final public boolean __replace(@Nullable E entity) {
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
    NamedParameterJdbcTemplate jdbcTemplate = getMasterConnection().getNamedParameterJdbcTemplate();
    int rows = jdbcTemplate.update(sql, parameterSource);
    return rows > 0;
  }

  public boolean __upsert(@Nullable final E entity) {
    if (entity == null) return false;
    ID id = getEntityId(entity);
    if (id == null) {
      __insert(entity);
      return true;
    }
    long millis = System.currentTimeMillis();
    touchCreateTime(entity, millis);
    touchUpdateTime(entity, millis);
    initializeRevision(entity);
    String tableName = getIdTableName(id);
    MysqlInsertOperation insertOperation = new MysqlInsertOperation(entityMapping, entity, tableName);
    MysqlUpdateOperation updateOperation = new MysqlUpdateOperation(entityMapping, entity, tableName);

    StringBuilder sql = new StringBuilder(insertOperation.generateSQL());
    String updateFields = updateOperation.toFields();
    Object[] params;
    if (StringUtils.isEmpty(updateFields)) {
      params = insertOperation.toParams();
    } else {
      sql.append(" ON DUPLICATE KEY UPDATE ").append(updateFields);
      List<Object> list = new ArrayList<>();
      Collections.addAll(list, insertOperation.toParams());
      Collections.addAll(list, updateOperation.toParams());
      params = list.toArray(new Object[0]);
    }
    JdbcTemplate jdbcTemplate = getMasterConnection().getJdbcTemplate();
    return jdbcTemplate.update(sql.toString(), params) > 0;
  }

  public boolean __delete(@Nullable final ID id) {
    if (id == null) return false;
    return __deletes(Collections.singleton(id)) > 0;
  }

  public long __deletes(@Nullable final Collection<ID> ids) {
    if (ids == null || ids.isEmpty()) return 0;
    if (ids.size() > 1) {
      AtomicLong deletedCount = new AtomicLong();
      getMasterConnection().executeTransactionWithoutResult(new TransactionCallbackWithoutResult() {
        @Override
        protected void doInTransactionWithoutResult(TransactionStatus status) {
          groupIds(ids).forEach((tableName, idList) -> {
            Criteria criteria;
            if (idList.size() == 1) {
              ID id = idList.iterator().next();
              criteria = Criteria.where(entityMapping.idField.getName()).is(id);
            } else {
              criteria = Criteria.where(entityMapping.idField.getName()).in(idList);
            }
            long rows = __executeDelete(criteria, tableName);
            deletedCount.addAndGet(rows);
          });
        }
      });
      return deletedCount.get();
    } else {
      ID id = ids.iterator().next();
      String tableName = getIdTableName(id);
      Criteria criteria = Criteria.where(entityMapping.idField.getName()).is(id);
      return __executeDelete(criteria, tableName);
    }
  }

  // ==========================================================================
  // Helper methods
  // ==========================================================================

  protected void __executeInsert(@NotNull final E entity,
                                 @NotNull final String tableName) {
    MysqlInsertOperation insertOperation = new MysqlInsertOperation(entityMapping, entity, tableName);
    String sql = insertOperation.generateSQL();
    PreparedStatementCreatorFactory factory = insertOperation.newPreparedStatementCreatorFactory(sql);
    if (generator == Id.Generator.AUTO_INC && !insertOperation.isIncludeId()) {
      factory.setReturnGeneratedKeys(true);
      PreparedStatementCreator creator = factory.newPreparedStatementCreator(insertOperation.toParams());
      GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
      JdbcTemplate jdbcTemplate = getMasterConnection().getJdbcTemplate();
      jdbcTemplate.update(creator, keyHolder);

      ConversionService conversionService = ConversionService.INSTANCE;
      Object id = conversionService.convert(keyHolder.getKey(), entityMapping.idClass);
      entityMapping.idField.setValue(entity, id);
    } else {
      PreparedStatementCreator creator = factory.newPreparedStatementCreator(insertOperation.toParams());
      JdbcTemplate jdbcTemplate = getMasterConnection().getJdbcTemplate();
      jdbcTemplate.update(creator);
    }
  }

  protected long __executeUpdate(@NotNull final Update update,
                                 @NotNull final Criteria criteria,
                                 @NotNull final String tableName) {
    NamedParameterSQL u = updateTranslator.translate(update, entityMapping);
    if (StringUtils.isEmpty(u.sql)) {
      throw new GoblinPersistenceException("No update SQL generated");
    }
    TranslatedCriteria c = criteriaTranslator.translate(criteria);

    MapSqlParameterSource source = new MapSqlParameterSource();
    source.addValues(u.source.getValues());
    source.addValues(c.parameterSource.getValues());

    String s = "UPDATE `%s` SET %s%s";
    s = String.format(s, tableName, u.sql, c.sql);
    AtomicReference<String> sql = new AtomicReference<>(s);
    NamedParameterJdbcTemplate jdbcTemplate = getMasterConnection().getNamedParameterJdbcTemplate();
    return jdbcTemplate.update(sql.get(), source);
  }

  protected long __executeDelete(@NotNull final Criteria criteria,
                                 @NotNull final String tableName) {
    TranslatedCriteria tc = criteriaTranslator.translate(criteria);
    MapSqlParameterSource source = new MapSqlParameterSource();
    source.addValues(tc.parameterSource.getValues());
    String s = "DELETE FROM `%s`%s";
    s = String.format(s, tableName, tc.sql);
    MutableObject<String> sql = new MutableObject<>(s);
    NamedParameterJdbcTemplate jdbcTemplate = getMasterConnection().getNamedParameterJdbcTemplate();
    return jdbcTemplate.update(sql.getValue(), source);
  }

  protected long __executeDelete(@NotNull Query query, @NotNull String tableName) {
    if (query.getNativeSQL() == null) {
      return __executeDelete(query.getCriteria(), tableName);
    }
    NativeSQL nativeSQL = query.getNativeSQL();
    MapSqlParameterSource source = new MapSqlParameterSource();
    if (nativeSQL.getParameters() != null) {
      source.addValues(nativeSQL.getParameters());
    }
    TranslatedCriteria tc = new TranslatedCriteria(nativeSQL.getSql(), source);

    String s = "DELETE FROM `%s`%s";
    s = String.format(s, tableName, tc.sql);
    AtomicReference<String> sql = new AtomicReference<>(s);
    NamedParameterJdbcTemplate jdbcTemplate = getMasterConnection().getNamedParameterJdbcTemplate();
    return jdbcTemplate.update(sql.get(), source);
  }

  protected long __executeCount(@NotNull final MysqlConnection connection,
                                @NotNull final Query query,
                                @NotNull final String tableName) {
    TranslatedCriteria tc = queryTranslator.translateCount(query, tableName);
    NamedParameterJdbcTemplate jdbcTemplate = connection.getNamedParameterJdbcTemplate();
    return jdbcTemplate.query(tc.sql, tc.parameterSource,
        (resultSet, i) -> resultSet.getLong(1)).iterator().next();
  }

  @NotNull
  protected List<E> __executeQuery(@NotNull final MysqlConnection connection,
                                   @NotNull final Query query,
                                   @NotNull final String tableName) {
    TranslatedCriteria tc = queryTranslator.translateQuery(query, tableName);
    NamedParameterJdbcTemplate jdbcTemplate = connection.getNamedParameterJdbcTemplate();
    return jdbcTemplate.query(tc.sql, tc.parameterSource, entityRowMapper);
  }
}
