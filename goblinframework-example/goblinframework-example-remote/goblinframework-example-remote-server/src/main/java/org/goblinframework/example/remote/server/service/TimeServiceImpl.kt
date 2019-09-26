package org.goblinframework.example.remote.server.service

import org.goblinframework.api.core.GoblinFuture
import org.goblinframework.api.remote.ExposeService
import org.goblinframework.api.remote.ExposeServices
import org.goblinframework.api.remote.ServiceVersion
import org.goblinframework.api.remote.ValueWrapperFuture
import org.goblinframework.example.remote.api.TimeService
import java.util.*
import javax.inject.Named

@Named
@ExposeServices(
    ExposeService(interfaceClass = TimeService::class, version = ServiceVersion(version = "1.0")),
    ExposeService(interfaceClass = TimeService::class, version = ServiceVersion(version = "2.0"))
)
class TimeServiceImpl : TimeService {

  override fun currentTimeMillis(): Long {
    return System.currentTimeMillis()
  }

  override fun currentDate(): GoblinFuture<Date> {
    return ValueWrapperFuture(Date())
  }
}