package org.goblinframework.bootstrap.core

import org.goblinframework.core.container.SpringContainer
import org.goblinframework.core.container.SpringContainerManager
import org.goblinframework.core.container.UseSpringContainer
import org.goblinframework.core.util.AnnotationUtils

object SpringContainerLoader {

  fun load(obj: Any): SpringContainer? {
    val annotation = AnnotationUtils.getAnnotation(obj::class.java, UseSpringContainer::class.java) ?: return null
    val configLocations = annotation.value
    return SpringContainerManager.INSTANCE.createStandaloneContainer(*configLocations)
  }

}