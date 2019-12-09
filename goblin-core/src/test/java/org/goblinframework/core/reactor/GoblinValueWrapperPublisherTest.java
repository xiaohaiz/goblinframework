package org.goblinframework.core.reactor;

import org.goblinframework.test.runner.GoblinTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.Assert.assertEquals;

@RunWith(GoblinTestRunner.class)
@ContextConfiguration("/UT.xml")
public class GoblinValueWrapperPublisherTest {

  @Test
  public void subscribe() {
    GoblinValueWrapperPublisher<String> publisher = new GoblinValueWrapperPublisher<>("HELLO");
    BlockingMonoSubscriber<String> subscriber = new BlockingMonoSubscriber<>();
    publisher.subscribe(subscriber);
    String s = subscriber.block();
    subscriber.dispose();
    assertEquals("HELLO", s);
  }
}