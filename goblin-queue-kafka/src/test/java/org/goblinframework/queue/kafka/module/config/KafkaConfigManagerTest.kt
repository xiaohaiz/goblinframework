package org.goblinframework.queue.kafka.module.config

import org.goblinframework.test.runner.GoblinTestRunner
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration

@RunWith(GoblinTestRunner::class)
@ContextConfiguration("/UT.xml")
class KafkaConfigManagerTest {

    @Test
    fun kafkaConfig() {
        val config = KafkaConfigManager.INSTANCE.getKafkaClient("_ut")
        Assert.assertNotNull(config)
    }
}