package org.goblinframework.monitor.module.test

import org.goblinframework.api.annotation.Install
import org.goblinframework.api.common.Ordered
import org.goblinframework.api.test.TestContext
import org.goblinframework.api.test.TestExecutionListener
import org.goblinframework.core.monitor.FlightId
import org.goblinframework.core.monitor.FlightLocation
import org.goblinframework.core.monitor.FlightRecorder
import org.goblinframework.core.monitor.StartPoint

@Install
class UnitTestFlightRecorder : TestExecutionListener, Ordered {

  override fun getOrder(): Int {
    return Ordered.HIGHEST_PRECEDENCE
  }

  override fun beforeTestMethod(testContext: TestContext) {
    val flightId = FlightLocation.builder()
        .startPoint(StartPoint.UTM)
        .clazz(testContext.testClass)
        .method(testContext.testMethod)
        .build()
        .launch()
    flightId?.run {
      synchronized(lock) {
        flightIds.add(this)
      }
    }
  }

  override fun afterTestMethod(testContext: TestContext) {
    FlightRecorder.terminateFlight()?.run {
      val flightId = this.flightId()
      synchronized(lock) {
        while (flightIds.contains(flightId)) {
          lock.wait()
        }
      }
    }
  }

  companion object {
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
}