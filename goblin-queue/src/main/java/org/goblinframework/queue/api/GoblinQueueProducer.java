package org.goblinframework.queue.api;

import org.goblinframework.queue.GoblinMessageSerializer;
import org.goblinframework.queue.QueueSystem;

import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(GoblinQueueProducers.class)
public @interface GoblinQueueProducer {

  QueueSystem system() default QueueSystem.KFK;

  String config() default "primary";

  String queue();

  GoblinMessageSerializer serializer() default GoblinMessageSerializer.HESSIAN2;

  boolean enabled() default true;
}
