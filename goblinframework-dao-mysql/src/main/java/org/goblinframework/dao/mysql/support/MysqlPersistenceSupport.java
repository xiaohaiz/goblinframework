package org.goblinframework.dao.mysql.support;

import org.goblinframework.api.annotation.Id;
import org.goblinframework.core.conversion.ConversionService;
import org.goblinframework.dao.mysql.cql.MysqlInsertOperation;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import java.util.Collection;
import java.util.Collections;

abstract public class MysqlPersistenceSupport<E, ID> extends MysqlPrimaryKeySupport<E, ID> {

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
}
