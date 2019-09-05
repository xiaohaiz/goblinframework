package org.goblinframework.core.management;

import org.goblinframework.core.util.AnnotationUtils;
import org.goblinframework.core.util.ClassUtils;

import java.lang.management.PlatformManagedObject;

abstract public class GoblinManagedObject implements PlatformManagedObject {

  protected GoblinManagedObject() {
    Class<?> clazz = ClassUtils.filterCglibProxyClass(getClass());
    GoblinManagedBean annotation = AnnotationUtils.getAnnotation(clazz, GoblinManagedBean.class);

  }
}
