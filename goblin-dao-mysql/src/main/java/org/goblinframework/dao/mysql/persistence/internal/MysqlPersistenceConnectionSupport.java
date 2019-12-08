package org.goblinframework.dao.mysql.persistence.internal;

import org.goblinframework.core.util.AnnotationUtils;
import org.goblinframework.core.util.ClassUtils;
import org.goblinframework.dao.annotation.PersistenceConnection;
import org.goblinframework.dao.mysql.exception.GoblinMysqlPersistenceException;
import org.goblinframework.database.mysql.client.MysqlClient;
import org.goblinframework.database.mysql.client.MysqlClientManager;
import org.goblinframework.database.mysql.client.MysqlMasterConnection;
import org.goblinframework.database.mysql.client.MysqlSlaveConnection;
import org.jetbrains.annotations.NotNull;

abstract public class MysqlPersistenceConnectionSupport<E, ID> extends MysqlPersistenceEntityMappingSupport<E, ID> {

  protected final MysqlClient client;

  protected MysqlPersistenceConnectionSupport() {
    Class<?> clazz = ClassUtils.filterCglibProxyClass(getClass());
    PersistenceConnection annotation = AnnotationUtils.getAnnotation(clazz, PersistenceConnection.class);
    if (annotation == null) {
      throw new GoblinMysqlPersistenceException("No @PersistenceConnection presented on " + clazz.getName());
    }
    String connection = annotation.connection();
    MysqlClient client = MysqlClientManager.INSTANCE.getMysqlClient(connection);
    if (client == null) {
      throw new GoblinMysqlPersistenceException("MysqlClient [" + connection + "] not found");
    }
    this.client = client;
  }

  @NotNull
  public MysqlMasterConnection getMasterConnection() {
    return client.getMasterConnection();
  }

  @NotNull
  public MysqlSlaveConnection createSlaveConnection() {
    return client.createSlaveConnection();
  }
}
