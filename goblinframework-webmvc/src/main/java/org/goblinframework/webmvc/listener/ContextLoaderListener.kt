package org.goblinframework.webmvc.listener

import org.goblinframework.core.bootstrap.GoblinBootstrap
import org.goblinframework.webmvc.container.GoblinWebApplicationContext
import javax.servlet.ServletContext
import javax.servlet.ServletContextEvent

open class ContextLoaderListener : org.springframework.web.context.ContextLoaderListener() {

  override fun determineContextClass(servletContext: ServletContext?): Class<*> {
    return GoblinWebApplicationContext::class.java
  }

  override fun contextInitialized(event: ServletContextEvent) {
    GoblinBootstrap.initialize()
    super.contextInitialized(event)
  }

  override fun contextDestroyed(event: ServletContextEvent) {
    super.contextDestroyed(event)
    GoblinBootstrap.close()
  }
}