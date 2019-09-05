package org.goblinframework.core.management;

import org.goblinframework.core.util.AnnotationUtils;
import org.goblinframework.core.util.ClassUtils;
import org.goblinframework.core.util.ManagementUtils;
import org.goblinframework.core.util.StringUtils;

import javax.management.ObjectName;
import java.lang.management.PlatformManagedObject;

abstract public class GoblinManagedObject implements PlatformManagedObject {

  private final ObjectName objectName;
  private final boolean registerMBean;

  protected GoblinManagedObject() {
    Class<?> clazz = ClassUtils.filterCglibProxyClass(getClass());
    GoblinManagedBean annotation = AnnotationUtils.getAnnotation(clazz, GoblinManagedBean.class);
    if (annotation != null) {
      String type = StringUtils.defaultIfBlank(annotation.type(), "GOBLIN");
      String name = StringUtils.defaultIfBlank(annotation.name(), "UNNAMED");
      objectName = ObjectNameGenerator.INSTANCE.generate(type, name);
    } else {
      objectName = null;
    }
    registerMBean = (objectName != null && annotation.register());
    if (registerMBean) {
      ManagementUtils.registerMBean(this);
    }
  }

  @Override
  public ObjectName getObjectName() {
    return objectName;
  }

  public void unregisterMbeanIfNecessary() {
    if (registerMBean) {
      ManagementUtils.unregisterMBean(objectName);
    }
  }
}
