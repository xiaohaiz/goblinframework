package org.goblinframework.queue.api;

import org.goblinframework.queue.QueueSystem;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface GoblinQueueConsumer {

  QueueSystem system() default QueueSystem.KFK;

  String config() default "primary";

  String queue();

  boolean enabled() default true;
}
