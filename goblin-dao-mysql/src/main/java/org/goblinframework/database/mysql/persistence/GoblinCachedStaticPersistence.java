package org.goblinframework.database.mysql.persistence;

import org.goblinframework.database.core.eql.Criteria;
import org.goblinframework.database.core.eql.Query;
import org.goblinframework.database.core.eql.Update;
import org.goblinframework.database.mysql.support.MysqlCachedPersistenceSupport;
import org.jetbrains.annotations.NotNull;

import java.util.List;

abstract public class GoblinCachedStaticPersistence<E, ID> extends MysqlCachedPersistenceSupport<E, ID> {

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
    return __executeQuery(getMasterConnection(), query, getTableName());
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
