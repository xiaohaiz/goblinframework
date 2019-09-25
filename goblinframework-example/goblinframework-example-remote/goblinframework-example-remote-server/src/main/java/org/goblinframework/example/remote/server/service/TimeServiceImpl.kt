package org.goblinframework.example.remote.server.service

import org.goblinframework.api.remote.ExposeService
import org.goblinframework.example.remote.api.TimeService
import javax.inject.Named

@Named
@ExposeService(interfaceClass = TimeService::class)
class TimeServiceImpl : TimeService {

  override fun currentTimeMillis(): Long {
    return System.currentTimeMillis()
  }
}