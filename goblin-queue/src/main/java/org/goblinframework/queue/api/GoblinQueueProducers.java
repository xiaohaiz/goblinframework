package org.goblinframework.queue.api;

import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface GoblinQueueProducers {

  GoblinQueueProducer[] value();
}
