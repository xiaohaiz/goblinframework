package org.goblinframework.queue.api;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface GoblinQueueConsumers {
  GoblinQueueConsumer[] consumers();

  GoblinConsumerMode mode();

  // 连接数，因为是NIO的，一般情况1个就够了
  int maxConcurrentConsumers() default 1;

  // 同时并发数
  int maxPermits() default 4;

  boolean enabled() default true;

  // group
  // mode=PUBSUB, 默认应用名
  // mode=QUEUE, 默认queue name
  // 熟悉kafka group原理的，可以自行配置，自行配置后，会覆盖默认行为
  String group() default "";
}
