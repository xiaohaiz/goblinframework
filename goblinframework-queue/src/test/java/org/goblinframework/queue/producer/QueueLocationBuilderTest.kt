package org.goblinframework.queue.producer

import org.goblinframework.core.util.GoblinField
import org.goblinframework.test.runner.GoblinTestRunner
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration

@RunWith(GoblinTestRunner::class)
@ContextConfiguration("/UT.xml")
class QueueLocationBuilderTest {

    @GoblinQueueProducer("test1", config = "_ut")
    @GoblinQueueProducer("test2", config = "_ut")
    private var producer: QueueProducer? = null

    @Test
    fun test() {
        val field = GoblinField(QueueLocationBuilderTest::class.java.getField("producer"))
        QueueProducerDefinitionBuilder.build(field)
    }
}