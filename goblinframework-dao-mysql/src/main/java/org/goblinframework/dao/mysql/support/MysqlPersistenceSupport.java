package org.goblinframework.dao.mysql.support;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.goblinframework.api.annotation.Id;
import org.goblinframework.core.conversion.ConversionService;
import org.goblinframework.dao.core.cql.Criteria;
import org.goblinframework.dao.core.cql.Query;
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
  public E $load(@Nullable final ID id) {
    if (id == null) return null;
    return $loads(Collections.singleton(id)).get(id);
  }

  @NotNull
  public Map<ID, E> $loads(@Nullable final Collection<ID> ids) {
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
      entities.addAll(executeQuery(query, tableName));
    });
    Map<ID, E> map = entities.stream()
        .collect(Collectors.toMap(this::getEntityId, Function.identity()));
    Map<ID, E> result = new LinkedHashMap<>();
    ids.stream()
        .map(id -> {
          E e = map.get(id);
          if (e == null) return null;
          return ImmutablePair.of(id, e);
        })
        .filter(Objects::nonNull)
        .forEach(p -> result.put(p.left, p.right));
    return result;
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
    if (isNestedTransactionEnabled()) {
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

  private List<E> executeQuery(final Query query, final String tableName) {
    TranslatedCriteria tc = queryTranslator.translateQuery(query, tableName);
    NamedParameterJdbcTemplate jdbcTemplate = client.getMasterNamedParameterJdbcTemplate();
    return jdbcTemplate.query(tc.sql, tc.parameterSource, entityRowMapper);
  }
}
