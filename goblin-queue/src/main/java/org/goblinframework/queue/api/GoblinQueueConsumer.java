package org.goblinframework.queue.api;

import org.goblinframework.queue.QueueSystem;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface GoblinQueueConsumer {

  QueueSystem system() default QueueSystem.KFK;

  GoblinConsumerMode mode();

  String config();

  String queue();

  // group
  // mode=PUBSUB, 默认应用名
  // mode=QUEUE, 默认queue name
  // 熟悉kafka group原理的，可以自行配置，自行配置后，会覆盖默认行为
  String group() default "";

  boolean enabled() default true;
}
