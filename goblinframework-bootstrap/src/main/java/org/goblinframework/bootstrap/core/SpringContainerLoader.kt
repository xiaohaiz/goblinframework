package org.goblinframework.bootstrap.core

import org.goblinframework.core.container.GoblinApplicationContext
import org.goblinframework.core.container.SpringContainer
import org.goblinframework.core.util.AnnotationUtils
import org.springframework.context.ApplicationContext

object SpringContainerLoader {

  fun load(obj: Any): ApplicationContext? {
    val annotation = AnnotationUtils.findAnnotation(obj::class.java, SpringContainer::class.java) ?: return null
    val configLocations = annotation.value
    return GoblinApplicationContext(*configLocations)
  }

}