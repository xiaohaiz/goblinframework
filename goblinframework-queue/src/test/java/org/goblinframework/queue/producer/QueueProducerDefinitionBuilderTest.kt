package org.goblinframework.queue.producer

import org.goblinframework.core.util.GoblinField
import org.goblinframework.core.util.ReflectionUtils
import org.goblinframework.test.runner.GoblinTestRunner
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration

@RunWith(GoblinTestRunner::class)
@ContextConfiguration("/UT.xml")
class QueueProducerDefinitionBuilderTest {

    @GoblinQueueProducer("test1", config = "_ut")
//    @GoblinQueueProducer("test2", config = "_ut")
    private lateinit var producer: QueueProducer

    @Test
    fun test() {
        val allFields = ReflectionUtils.allFieldsIncludingAncestors(this::class.java, false, false)
        QueueProducerDefinitionBuilder.build(GoblinField(allFields.get(0)))
    }
}