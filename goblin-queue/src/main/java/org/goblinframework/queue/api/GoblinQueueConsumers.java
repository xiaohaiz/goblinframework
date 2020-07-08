package org.goblinframework.queue.api;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface GoblinQueueConsumers {
  GoblinQueueConsumer[] consumers();

  // 连接数，因为是NIO的，一般情况1个就够了
  int maxConcurrentConsumers() default 1;

  // 同时并发数
  int maxPermits() default 4;

  boolean enabled() default true;
}
