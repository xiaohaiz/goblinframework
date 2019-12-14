package org.goblinframework.dao.mysql.persistence;

import org.goblinframework.dao.mysql.persistence.internal.MysqlPersistenceOperationSupport;
import org.goblinframework.dao.ql.Criteria;
import org.goblinframework.dao.ql.Query;
import org.goblinframework.dao.ql.Update;
import org.jetbrains.annotations.NotNull;

import java.util.List;

abstract public class GoblinStaticPersistence<E, ID> extends MysqlPersistenceOperationSupport<E, ID> {

  @NotNull
  @Override
  protected String calculateTableName(@NotNull String template, @NotNull E document) {
    return getEntityTableName(null);
  }

  public String getTableName() {
    return getEntityTableName(null);
  }

  public long directCount() {
    return directCount(Query.query(new Criteria()));
  }

  public long directCount(@NotNull Query query) {
    return __executeCount(getMasterConnection(), query, getTableName());
  }

  @NotNull
  public List<E> directQuery() {
    return directQuery(Query.query(new Criteria()));
  }

  @NotNull
  public List<E> directQuery(@NotNull Query query) {
    return __find(getMasterConnection(), getTableName(), query);
  }

  public long directUpdate(@NotNull Update update, @NotNull Criteria criteria) {
    return __executeUpdate(update, criteria, getTableName());
  }

  public long directRemove(@NotNull Query query) {
    return __executeDelete(query, getTableName());
  }

  public long directRemove(@NotNull Criteria criteria) {
    return __executeDelete(criteria, getTableName());
  }

}
