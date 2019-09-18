package org.goblinframework.core.mbean;

import org.goblinframework.api.common.Disposable;
import org.goblinframework.api.common.Initializable;
import org.goblinframework.core.util.AnnotationUtils;
import org.goblinframework.core.util.ClassUtils;
import org.goblinframework.core.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.ObjectName;
import java.lang.management.PlatformManagedObject;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

abstract public class GoblinManagedObject
    implements PlatformManagedObject, Initializable, Disposable {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  private final ObjectName objectName;
  private final boolean registerMBean;
  private final AtomicBoolean initialized = new AtomicBoolean();
  private final AtomicBoolean disposed = new AtomicBoolean();

  protected GoblinManagedObject() {
    Class<?> clazz = ClassUtils.filterCglibProxyClass(getClass());
    GoblinManagedBean annotation = AnnotationUtils.findAnnotation(clazz, GoblinManagedBean.class);
    if (annotation != null) {
      String type = StringUtils.defaultIfBlank(annotation.type(), "GOBLIN");
      String name = StringUtils.defaultIfBlank(annotation.name(),
          (Supplier<String>) () -> StringUtils.defaultIfBlank(clazz.getSimpleName(), "UNNAMED"));
      assert name != null;
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

  @Override
  final public void initialize() {
    if (initialized.compareAndSet(false, true)) {
      initializeBean();
    }
  }

  @Override
  final public void dispose() {
    if (disposed.compareAndSet(false, true)) {
      if (registerMBean) {
        ManagedBeanUtils.unregisterMBean(objectName);
      }
      disposeBean();
    }
  }

  protected void initializeBean() {
  }

  protected void disposeBean() {
  }
}
