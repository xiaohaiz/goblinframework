package org.goblinframework.example.remote.client.service

import org.goblinframework.api.remote.ImportService
import org.goblinframework.core.container.SpringManagedBean
import org.goblinframework.example.remote.api.TimeService
import javax.inject.Named

@Named
class TimeServiceClient : SpringManagedBean() {

  @ImportService(interfaceClass = TimeService::class)
  lateinit var timeService: TimeService

}
