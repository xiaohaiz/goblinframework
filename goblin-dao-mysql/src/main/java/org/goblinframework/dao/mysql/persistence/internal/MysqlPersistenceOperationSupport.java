package org.goblinframework.dao.mysql.persistence.internal;

import org.apache.commons.lang3.mutable.MutableObject;
import org.goblinframework.api.dao.Id;
import org.goblinframework.core.conversion.ConversionService;
import org.goblinframework.core.util.MapUtils;
import org.goblinframework.core.util.StringUtils;
import org.goblinframework.dao.mapping.EntityRevisionField;
import org.goblinframework.dao.mysql.client.MysqlConnection;
import org.goblinframework.dao.mysql.exception.GoblinMysqlPersistenceException;
import org.goblinframework.dao.mysql.mapping.MysqlEntityRowMapper;
import org.goblinframework.dao.mysql.ql.*;
import org.goblinframework.dao.ql.Criteria;
import org.goblinframework.dao.ql.NativeSQL;
import org.goblinframework.dao.ql.Query;
import org.goblinframework.dao.ql.Update;
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
import org.springframework.util.LinkedMultiValueMap;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

abstract public class MysqlPersistenceOperationSupport<E, ID> extends MysqlPersistencePrimaryKeySupport<E, ID> {

  protected final RowMapper<E> entityRowMapper;
  protected final MysqlCriteriaTranslator criteriaTranslator;
  protected final MysqlQueryTranslator queryTranslator;
  protected final MysqlUpdateTranslator updateTranslator;

  protected MysqlPersistenceOperationSupport() {
    entityRowMapper = new MysqlEntityRowMapper<>(entityMapping);
    criteriaTranslator = MysqlCriteriaTranslator.INSTANCE;
    queryTranslator = new MysqlQueryTranslator(entityMapping);
    updateTranslator = MysqlUpdateTranslator.INSTANCE;
  }

  public void insert(@NotNull final E entity) {
    __insert(entity);
  }

  public void inserts(@NotNull final Collection<E> entities) {
    __inserts(entities);
  }

  @Nullable
  public E load(@NotNull final ID id) {
    return __load(id, getMasterConnection());
  }

  @NotNull
  public Map<ID, E> loads(@NotNull final Collection<ID> ids) {
    return __loads(ids, getMasterConnection());
  }

  public boolean exists(@NotNull final ID id) {
    return __exists(id, getMasterConnection());
  }

  public boolean replace(@NotNull final E entity) {
    return __replace(entity);
  }

  public boolean upsert(@NotNull final E entity) {
    return __upsert(entity);
  }

  public boolean delete(@NotNull final ID id) {
    return __delete(id);
  }

  public long deletes(@NotNull final Collection<ID> ids) {
    return __deletes(ids);
  }

  // ==========================================================================
  // Direct database access methods
  // ==========================================================================

  final public void __insert(@NotNull final E entity) {
    beforeInsert(entity);
    String tableName = getEntityTableName(entity);
    __insert(tableName, entity);
  }

  final public void __inserts(@NotNull final Collection<E> entities) {
    List<E> candidates = entities.stream().filter(Objects::nonNull).collect(Collectors.toList());
    if (candidates.isEmpty()) {
      return;
    }
    if (candidates.size() == 1) {
      E entity = candidates.iterator().next();
      __insert(entity);
      return;
    }
    candidates.forEach(this::beforeInsert);
    getMasterConnection().executeTransactionWithoutResult(new TransactionCallbackWithoutResult() {
      @Override
      protected void doInTransactionWithoutResult(@NotNull TransactionStatus status) {
        groupEntities(candidates).forEach((tableName, list) -> {
          for (E e : list) {
            __insert(tableName, e);
          }
        });
      }
    });
  }

  @Nullable
  final public E __load(@NotNull final ID id, @Nullable final MysqlConnection connection) {
    AtomicReference<MysqlConnection> connectionReference = new AtomicReference<>(connection);
    if (connection == null) {
      connectionReference.set(getMasterConnection());
    }
    String tableName = getIdTableName(id);
    Criteria criteria = Criteria.where(entityMapping.getIdFieldName()).is(id);
    Query query = Query.query(criteria);
    List<E> entities = __find(connectionReference.get(), tableName, query);
    return entities.stream().findFirst().orElse(null);
  }

  @NotNull
  final public Map<ID, E> __loads(@NotNull final Collection<ID> ids, @Nullable final MysqlConnection connection) {
    List<ID> idList = ids.stream().filter(Objects::nonNull).collect(Collectors.toList());
    if (idList.isEmpty()) {
      return Collections.emptyMap();
    }
    if (idList.size() == 1) {
      ID id = idList.iterator().next();
      E entity = __load(id, connection);
      if (entity == null) {
        return Collections.emptyMap();
      } else {
        Map<ID, E> entities = new LinkedHashMap<>();
        entities.put(id, entity);
        return entities;
      }
    }
    AtomicReference<MysqlConnection> connectionReference = new AtomicReference<>(connection);
    if (connection == null) {
      connectionReference.set(getMasterConnection());
    }
    LinkedMultiValueMap<String, ID> groupedIds = groupIds(idList);
    List<E> entityList = new ArrayList<>();
    groupedIds.forEach((tn, ds) -> {
      Criteria criteria;
      if (ds.size() == 1) {
        ID id = ds.iterator().next();
        criteria = Criteria.where(entityMapping.getIdFieldName()).is(id);
      } else {
        criteria = Criteria.where(entityMapping.getIdFieldName()).in(ds);
      }
      Query query = Query.query(criteria);
      List<E> queried = __find(connectionReference.get(), tn, query);
      entityList.addAll(queried);
    });
    Map<ID, E> entities = new LinkedHashMap<>();
    entityList.forEach(e -> {
      ID id = getEntityId(e);
      entities.put(id, e);
    });
    return MapUtils.resort(entities, idList);
  }

  final public boolean __exists(@NotNull final ID id,
                                @Nullable final MysqlConnection connection) {
    AtomicReference<MysqlConnection> connectionReference = new AtomicReference<>(connection);
    if (connection == null) {
      connectionReference.set(getMasterConnection());
    }
    Criteria criteria = Criteria.where(entityMapping.getIdFieldName()).is(id);
    Query query = Query.query(criteria);
    String tableName = getIdTableName(id);
    return __count(connectionReference.get(), tableName, query) > 0;
  }

  final public boolean __replace(@NotNull final E entity) {
    ID id = getEntityId(entity);
    if (id == null) {
      throw new GoblinMysqlPersistenceException("ID must not be null when executing replace operation");
    }
    beforeReplace(entity);
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
      throw new GoblinMysqlPersistenceException("Nothing field(s) found when executing replace operation");
    }
    String sql = tc2.sql + tc1.sql;
    MapSqlParameterSource parameterSource = new MapSqlParameterSource();
    parameterSource.addValues(tc2.parameterSource.getValues());
    parameterSource.addValues(tc1.parameterSource.getValues());
    NamedParameterJdbcTemplate jdbcTemplate = getMasterConnection().getNamedParameterJdbcTemplate();
    int rows = jdbcTemplate.update(sql, parameterSource);
    return rows > 0;
  }

  final public boolean __upsert(@NotNull final E entity) {
    ID id = getEntityId(entity);
    if (id == null) {
      __insert(entity);
      return true;
    }
    beforeInsert(entity);
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

  final public boolean __delete(@NotNull final ID id) {
    Criteria criteria = Criteria.where(entityMapping.idField.getName()).is(id);
    String tableName = getIdTableName(id);
    long rows = __delete(tableName, criteria);
    return rows > 0;
  }

  final public long __deletes(@NotNull final Collection<ID> ids) {
    List<ID> idList = ids.stream().filter(Objects::nonNull).collect(Collectors.toList());
    if (idList.isEmpty()) {
      return 0;
    }
    if (idList.size() == 1) {
      ID id = idList.stream().findFirst().orElse(null);
      boolean success = __delete(id);
      return success ? 1 : 0;
    }
    AtomicLong deletedCount = new AtomicLong();
    getMasterConnection().executeTransactionWithoutResult(new TransactionCallbackWithoutResult() {
      @Override
      protected void doInTransactionWithoutResult(@NotNull TransactionStatus status) {
        groupIds(idList).forEach((tableName, ds) -> {
          Criteria criteria = Criteria.where(entityMapping.idField.getName()).in(ds);
          long rows = __delete(tableName, criteria);
          deletedCount.addAndGet(rows);
        });
      }
    });
    return deletedCount.get();
  }

  final protected void __insert(@NotNull final String tableName,
                                @NotNull final E entity) {
    MysqlInsertOperation insertOperation = new MysqlInsertOperation(entityMapping, entity, tableName);
    String sql = insertOperation.generateSQL();
    PreparedStatementCreatorFactory factory = insertOperation.newPreparedStatementCreatorFactory(sql);
    if (idGenerator == Id.Generator.AUTO_INC && !insertOperation.isIncludeId()) {
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

  final protected long __update(@NotNull final String tableName,
                                @NotNull final Update update,
                                @NotNull final Criteria criteria) {
    NamedParameterSQL u = updateTranslator.translate(update, entityMapping);
    if (StringUtils.isEmpty(u.sql)) {
      throw new GoblinMysqlPersistenceException("No update SQL generated");
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

  final protected long __delete(@NotNull final String tableName,
                                @NotNull final Criteria criteria) {
    TranslatedCriteria tc = criteriaTranslator.translate(criteria);
    MapSqlParameterSource source = new MapSqlParameterSource();
    source.addValues(tc.parameterSource.getValues());
    String s = "DELETE FROM `%s`%s";
    s = String.format(s, tableName, tc.sql);
    MutableObject<String> sql = new MutableObject<>(s);
    NamedParameterJdbcTemplate jdbcTemplate = getMasterConnection().getNamedParameterJdbcTemplate();
    return jdbcTemplate.update(sql.getValue(), source);
  }

  final protected long __delete(@NotNull final String tableName,
                                @NotNull final Query query) {
    if (query.getNativeSQL() == null) {
      return __delete(tableName, query.getCriteria());
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

  final protected long __count(@NotNull final MysqlConnection connection,
                               @NotNull final String tableName,
                               @NotNull final Query query) {
    TranslatedCriteria tc = queryTranslator.translateCount(query, tableName);
    NamedParameterJdbcTemplate jdbcTemplate = connection.getNamedParameterJdbcTemplate();
    return jdbcTemplate.query(tc.sql, tc.parameterSource,
        (resultSet, i) -> resultSet.getLong(1)).iterator().next();
  }

  @NotNull
  final protected List<E> __find(@NotNull final MysqlConnection connection,
                                 @NotNull final String tableName,
                                 @NotNull final Query query) {
    TranslatedCriteria tc = queryTranslator.translateQuery(query, tableName);
    NamedParameterJdbcTemplate jdbcTemplate = connection.getNamedParameterJdbcTemplate();
    return jdbcTemplate.query(tc.sql, tc.parameterSource, entityRowMapper);
  }
}
