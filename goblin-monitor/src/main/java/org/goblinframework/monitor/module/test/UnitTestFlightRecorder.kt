package org.goblinframework.monitor.module.test

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.api.function.Ordered
import org.goblinframework.api.test.TestContext
import org.goblinframework.api.test.TestExecutionListener
import org.goblinframework.core.monitor.Flight.StartPoint
import org.goblinframework.core.monitor.FlightId
import org.goblinframework.core.monitor.FlightLocation
import org.goblinframework.core.monitor.FlightRecorder

@Singleton
class UnitTestFlightRecorder private constructor() : TestExecutionListener, Ordered {

  companion object {
    @JvmField val INSTANCE = UnitTestFlightRecorder()
    private val lock = Object()
    private val flightIds = LinkedHashSet<FlightId>()

    fun onFlightFinished(flightId: FlightId) {
      synchronized(lock) {
        if (flightIds.remove(flightId)) {
          lock.notifyAll()
        }
      }
    }
  }

  override fun getOrder(): Int {
    return Ordered.HIGHEST_PRECEDENCE
  }

  override fun beforeTestMethod(testContext: TestContext) {
    val flightId = FlightLocation.builder()
        .startPoint(StartPoint.UTM)
        .clazz(testContext.getTestClass())
        .method(testContext.getTestMethod())
        .build()
        .launch()
    flightId?.run {
      synchronized(lock) {
        flightIds.add(this)
      }
    }
  }

  override fun afterTestMethod(testContext: TestContext) {
    FlightRecorder.getFlightMonitor()?.terminateFlight()?.run {
      val flightId = flightId()
      synchronized(lock) {
        while (flightIds.contains(flightId)) {
          lock.wait()
        }
      }
    }
  }

}