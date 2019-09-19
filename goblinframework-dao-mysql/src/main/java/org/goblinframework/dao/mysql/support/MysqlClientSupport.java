package org.goblinframework.dao.mysql.support;

import org.goblinframework.core.util.AnnotationUtils;
import org.goblinframework.core.util.ClassUtils;
import org.goblinframework.dao.mysql.client.MysqlClient;
import org.goblinframework.dao.mysql.client.MysqlClientManager;
import org.goblinframework.dao.mysql.persistence.GoblinPersistenceException;

import java.util.concurrent.atomic.AtomicBoolean;

abstract public class MysqlClientSupport<E, ID> extends MysqlEntityMappingSupport<E, ID> {

  private final AtomicBoolean nestedTransaction = new AtomicBoolean(true);
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

  protected void turnOffNestedTransaction() {
    nestedTransaction.set(false);
  }

  protected void turnOnNestedTransaction() {
    nestedTransaction.set(true);
  }

  protected boolean isNestedTransactionEnabled() {
    return nestedTransaction.get();
  }
}
