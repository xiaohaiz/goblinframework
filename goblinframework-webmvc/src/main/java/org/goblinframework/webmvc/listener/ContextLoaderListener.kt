package org.goblinframework.webmvc.listener

import org.goblinframework.core.bootstrap.GoblinBootstrap
import org.goblinframework.webmvc.container.WebappSpringContainer
import javax.servlet.ServletContext
import javax.servlet.ServletContextEvent

open class ContextLoaderListener : org.springframework.web.context.ContextLoaderListener() {

  override fun determineContextClass(servletContext: ServletContext?): Class<*> {
    return WebappSpringContainer::class.java
  }

  override fun contextInitialized(event: ServletContextEvent) {
    GoblinBootstrap.doInitialize()
    GoblinBootstrap.doBootstrap()
    super.contextInitialized(event)
  }

  override fun contextDestroyed(event: ServletContextEvent) {
    GoblinBootstrap.doShutdown()
    super.contextDestroyed(event)
    GoblinBootstrap.doFinalize()
  }
}