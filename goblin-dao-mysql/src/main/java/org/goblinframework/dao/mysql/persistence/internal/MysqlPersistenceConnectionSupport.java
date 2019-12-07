package org.goblinframework.dao.mysql.persistence.internal;

import org.goblinframework.core.util.AnnotationUtils;
import org.goblinframework.core.util.ClassUtils;
import org.goblinframework.dao.core.annotation.GoblinConnection;
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
    GoblinConnection annotation = AnnotationUtils.getAnnotation(clazz, GoblinConnection.class);
    if (annotation == null) {
      throw new GoblinMysqlPersistenceException("No @GoblinDatabaseConnection presented on " + clazz.getName());
    }
    String name = annotation.name();
    MysqlClient client = MysqlClientManager.INSTANCE.getMysqlClient(name);
    if (client == null) {
      throw new GoblinMysqlPersistenceException("MysqlClient [" + name + "] not found");
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
