package org.goblinframework.queue.producer;

import org.goblinframework.core.util.GoblinField;
import org.goblinframework.core.util.ReflectionUtils;
import org.goblinframework.queue.api.GoblinQueueProducer;
import org.goblinframework.queue.api.QueueProducer;
import org.goblinframework.test.runner.GoblinTestRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;

import java.lang.reflect.Field;
import java.util.List;

@RunWith(GoblinTestRunner.class)
@ContextConfiguration("/UT.xml")
public class QueueProducerDefinitionBuilderTest {

  @GoblinQueueProducer(queue = "test1", config = "_ut")
  @GoblinQueueProducer(queue = "test2", config = "_ut")
  private QueueProducer producer;

  @Test
  public void test() {
    List<Field> fields = ReflectionUtils.allFieldsIncludingAncestors(this.getClass(), false, false);
    List<QueueProducerDefinition> definitions = QueueProducerDefinitionBuilder.Companion.build(new GoblinField(fields.get(0)));
    Assert.assertEquals(2, definitions.size());
  }
}
