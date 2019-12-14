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

  public long __count() {
    return __count(Query.query(new Criteria()));
  }

  public long __count(@NotNull Query query) {
    return __count(getMasterConnection(), getTableName(), query);
  }

  @NotNull
  public List<E> __find() {
    return __find(Query.query(new Criteria()));
  }

  @NotNull
  public List<E> __find(@NotNull Query query) {
    return __find(getMasterConnection(), getTableName(), query);
  }

  public long __update(@NotNull Update update, @NotNull Criteria criteria) {
    return __executeUpdate(update, criteria, getTableName());
  }

  public long __delete(@NotNull Query query) {
    return __executeDelete(query, getTableName());
  }

  public long __delete(@NotNull Criteria criteria) {
    return __executeDelete(criteria, getTableName());
  }

}
