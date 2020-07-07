package org.goblinframework.queue.api;

import java.lang.management.PlatformManagedObject;

public interface QueueProducerMXBean extends PlatformManagedObject {

    String getMessageType();

    String getLocation();

    String getSerializer();

    Long getSuccessCount();

    Long getFailureCount();

    void produceText(String text);
}