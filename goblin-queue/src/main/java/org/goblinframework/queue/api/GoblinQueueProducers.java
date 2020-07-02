package org.goblinframework.queue.api;

import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface GoblinQueueProducers {

  GoblinQueueProducer[] value();
}
