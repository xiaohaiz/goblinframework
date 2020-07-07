package org.goblinframework.example.queue.listener

import org.goblinframework.queue.QueueSystem
import org.goblinframework.queue.api.GoblinConsumerMode
import org.goblinframework.queue.api.GoblinQueueConsumer
import org.goblinframework.queue.api.GoblinQueueConsumers
import org.goblinframework.queue.api.QueueListener
import javax.inject.Named

/**
 * @author changyuan.liu
 * @since 2020/7/7
 */
@Named
@GoblinQueueConsumers(
        consumers = [
            GoblinQueueConsumer(system = QueueSystem.KFK, config = "default", queue = "test.example.low.level.queue", group = "test_hunb")
        ],
        mode = GoblinConsumerMode.PUBSUB,
        maxPermits = 2
)
class SampleQueueListener : QueueListener {
    override fun handle(data: ByteArray) {
        println(String(data, Charsets.UTF_8))
    }

}