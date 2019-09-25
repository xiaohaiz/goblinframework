package org.goblinframework.dao.mysql.persistence;

import org.goblinframework.dao.core.cql.Criteria;
import org.goblinframework.dao.core.cql.Query;
import org.goblinframework.dao.core.cql.Update;
import org.goblinframework.dao.mysql.support.MysqlPersistenceSupport;
import org.jetbrains.annotations.NotNull;

import java.util.List;

abstract public class GoblinStaticPersistence<E, ID> extends MysqlPersistenceSupport<E, ID> {

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
    return executeCount(getMasterConnection(), query, getTableName());
  }

  @NotNull
  public List<E> directQuery() {
    return directQuery(Query.query(new Criteria()));
  }

  @NotNull
  public List<E> directQuery(@NotNull Query query) {
    return executeQuery(getMasterConnection(), query, getTableName());
  }

  public long directUpdate(@NotNull Update update, @NotNull Criteria criteria) {
    return executeUpdate(update, criteria, getTableName());
  }

  public long directRemove(@NotNull Query query) {
    return executeDelete(query, getTableName());
  }

  public long directRemove(@NotNull Criteria criteria) {
    return executeDelete(criteria, getTableName());
  }

}
