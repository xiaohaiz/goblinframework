package org.goblinframework.example.remote.server.service

import org.goblinframework.api.concurrent.GoblinFuture
import org.goblinframework.api.rpc.ExposeService
import org.goblinframework.core.concurrent.GoblinValueWrapperFuture
import org.goblinframework.core.container.SpringContainerObject
import org.goblinframework.example.remote.api.TimeService
import org.springframework.stereotype.Service
import java.util.*

@Service
@ExposeService(interfaceClass = TimeService::class)
class TimeServiceImpl : SpringContainerObject(), TimeService {

  override fun currentTimeMillis(): Long {
    return System.currentTimeMillis()
  }

  override fun currentDate(): GoblinFuture<Date> {
    return GoblinValueWrapperFuture(Date())
  }
}