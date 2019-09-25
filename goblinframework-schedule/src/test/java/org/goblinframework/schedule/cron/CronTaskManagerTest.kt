package org.goblinframework.schedule.cron

import org.goblinframework.api.schedule.CronConstants
import org.goblinframework.api.schedule.CronTask
import org.goblinframework.core.util.RandomUtils
import org.goblinframework.test.runner.GoblinTestRunner
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration
import java.util.concurrent.CountDownLatch

@RunWith(GoblinTestRunner::class)
@ContextConfiguration("/UT.xml")
class CronTaskManagerTest {

  @Test
  fun cronTask() {
    val latch = CountDownLatch(1)
    val task = object : CronTask {
      override fun name(): String {
        return RandomUtils.nextObjectId()
      }

      override fun cronExpression(): String {
        return CronConstants.SECOND_TIMER
      }

      override fun execute() {
        latch.countDown()
      }

      override fun concurrent(): Boolean {
        return false
      }
    }
    CronTaskManager.INSTANCE.register(task)
    latch.await()
    CronTaskManager.INSTANCE.unregister(task.name())
  }
}