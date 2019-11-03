package org.goblinframework.queue.producer;

import org.goblinframework.test.runner.GoblinTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;

@RunWith(GoblinTestRunner.class)
@ContextConfiguration("/UT.xml")
public class QueueProducerDefinitionBuilderTest {

  @GoblinQueueProducer(queue = "test1", config = "_ut")
  @GoblinQueueProducer(queue = "test2", config = "_ut")
  private QueueProducer producer;

    @Test
    void test() {
//      val allFields = ReflectionUtils.allFieldsIncludingAncestors(this::class.java, false, false)
//      QueueProducerDefinitionBuilder.build(GoblinField(allFields.get(0)))
    }
}
