package org.goblinframework.queue.producer

import org.goblinframework.core.util.GoblinField
import org.goblinframework.test.runner.GoblinTestRunner
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration

@RunWith(GoblinTestRunner::class)
@ContextConfiguration("/UT.xml")
class QueueProducerDefinitionBuilderTest {

    @GoblinQueueProducer("test1", config = "_ut")
    @GoblinQueueProducer("test2", config = "_ut")
    private lateinit var producer: QueueProducer

    @Test
    fun test() {
        val field = GoblinField(QueueProducerDefinitionBuilderTest::class.java.getField("QueueProducer"))
        QueueProducerDefinitionBuilder.build(field)
    }
}