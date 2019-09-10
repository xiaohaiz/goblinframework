package org.goblinframework.bootstrap.core

import org.goblinframework.core.container.StandaloneSpringContainer
import org.goblinframework.core.container.UseSpringContainer
import org.goblinframework.core.util.AnnotationUtils
import org.springframework.context.ApplicationContext

object SpringContainerLoader {

  fun load(obj: Any): ApplicationContext? {
    val annotation = AnnotationUtils.findAnnotation(obj::class.java, UseSpringContainer::class.java) ?: return null
    val configLocations = annotation.value
    return StandaloneSpringContainer(*configLocations)
  }

}