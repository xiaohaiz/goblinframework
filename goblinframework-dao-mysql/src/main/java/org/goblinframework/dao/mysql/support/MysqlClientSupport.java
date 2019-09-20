package org.goblinframework.dao.mysql.support;

import org.goblinframework.core.util.AnnotationUtils;
import org.goblinframework.core.util.ClassUtils;
import org.goblinframework.dao.mysql.client.MysqlClient;
import org.goblinframework.dao.mysql.client.MysqlClientManager;
import org.goblinframework.dao.mysql.persistence.GoblinPersistenceException;

abstract public class MysqlClientSupport<E, ID> extends MysqlEntityMappingSupport<E, ID> {

  protected final MysqlClient client;

  protected MysqlClientSupport() {
    Class<?> clazz = ClassUtils.filterCglibProxyClass(getClass());
    UseMysqlClient annotation = AnnotationUtils.findAnnotation(clazz, UseMysqlClient.class);
    if (annotation == null) {
      throw new GoblinPersistenceException("No @UseMysqlClient presented on " + clazz.getName());
    }
    String name = annotation.value();
    MysqlClient client = MysqlClientManager.INSTANCE.getMysqlClient(name);
    if (client == null) {
      throw new GoblinPersistenceException("MysqlClient [" + name + "] not found");
    }
    this.client = client;
  }
}
