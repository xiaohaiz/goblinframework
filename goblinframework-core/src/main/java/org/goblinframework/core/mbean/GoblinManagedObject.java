package org.goblinframework.core.mbean;

import org.goblinframework.core.util.AnnotationUtils;
import org.goblinframework.core.util.ClassUtils;
import org.goblinframework.core.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.ObjectName;
import java.lang.management.PlatformManagedObject;
import java.util.function.Supplier;

abstract public class GoblinManagedObject implements PlatformManagedObject {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  private final ObjectName objectName;
  private final boolean registerMBean;

  protected GoblinManagedObject() {
    Class<?> clazz = ClassUtils.filterCglibProxyClass(getClass());
    GoblinManagedBean annotation = AnnotationUtils.findAnnotation(clazz, GoblinManagedBean.class);
    if (annotation != null) {
      String type = StringUtils.defaultIfBlank(annotation.type(), "GOBLIN");
      String name = StringUtils.defaultIfBlank(annotation.name(),
          (Supplier<String>) () -> StringUtils.defaultIfBlank(clazz.getSimpleName(), "UNNAMED"));
      objectName = ObjectNameGenerator.INSTANCE.generate(type, name);
    } else {
      objectName = null;
    }
    registerMBean = (objectName != null && annotation.register());
    if (registerMBean) {
      ManagedBeanUtils.registerMBean(this);
    }
  }

  @Override
  public ObjectName getObjectName() {
    return objectName;
  }

  public void unregisterIfNecessary() {
    if (registerMBean) {
      ManagedBeanUtils.unregisterMBean(objectName);
    }
  }
}
