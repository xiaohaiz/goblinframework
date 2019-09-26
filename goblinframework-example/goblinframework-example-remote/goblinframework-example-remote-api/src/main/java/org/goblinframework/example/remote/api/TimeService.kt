package org.goblinframework.example.remote.api

import org.goblinframework.api.common.GoblinFuture
import java.util.*

interface TimeService {

  fun currentTimeMillis(): Long

  fun currentDate(): GoblinFuture<Date>

}
