package org.goblinframework.remote.client.invocation.invoker.java;

import org.goblinframework.core.reactor.BlockingMonoSubscriber;
import org.goblinframework.core.reactor.CoreScheduler;
import org.goblinframework.test.runner.GoblinTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.Assert.assertEquals;

@RunWith(GoblinTestRunner.class)
@ContextConfiguration("/UT.xml")
public class RemoteJavaClientInvokerPublisherTest {

  @Test
  public void subscribe() {
    RemoteJavaClientInvokerFuture future = new RemoteJavaClientInvokerFuture(90000);
    RemoteJavaClientInvokerPublisher publisher = future.asPublisher();
    BlockingMonoSubscriber<Object> subscriber = new BlockingMonoSubscriber<>();
    publisher.subscribe(subscriber);
    CoreScheduler.getInstance().schedule(() -> future.complete("HELLO"));
    Object ret = subscriber.block();
    subscriber.dispose();
    assertEquals("HELLO", ret);
  }
}