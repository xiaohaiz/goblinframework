package org.goblinframework.queue.api;

import org.goblinframework.queue.QueueSystem;

import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(GoblinQueueProducers.class)
public @interface GoblinQueueProducer {

  QueueSystem system() default QueueSystem.KFK;

  String config() default "primary";

  String queue();

  boolean enabled() default true;
}
