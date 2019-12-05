package org.goblinframework.example.remote.api

import org.goblinframework.api.concurrent.GoblinFuture
import java.util.*

interface TimeService {

  fun currentTimeMillis(): Long

  fun currentDate(): GoblinFuture<Date>

}
