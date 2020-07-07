package org.goblinframework.queue.api;

import java.lang.management.PlatformManagedObject;

public interface QueueProducerMXBean extends PlatformManagedObject {

    String getProducerType();

    String getDefinition();

    Long getSuccessCount();

    Long getFailureCount();

    void produceText(String text);
}