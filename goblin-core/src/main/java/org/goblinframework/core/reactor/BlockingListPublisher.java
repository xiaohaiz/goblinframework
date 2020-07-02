package org.goblinframework.core.reactor;

import org.goblinframework.api.annotation.ThreadSafe;
import org.goblinframework.core.concurrent.GoblinInterruptedException;
import org.goblinframework.core.util.ExceptionUtils;
import org.goblinframework.core.util.GoblinReferenceCount;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * This is not real reactor publisher.
 */
@ThreadSafe
final public class BlockingListPublisher<T> extends CountDownLatch {

  private final List<T> queue = Collections.synchronizedList(new ArrayList<>());
  private final GoblinReferenceCount referenceCount;
  private Throwable error;

  public BlockingListPublisher() {
    this(1);
  }

  public BlockingListPublisher(int count) {
    super(1);
    if (count <= 0) {
      throw new IllegalArgumentException("count>0 required");
    }
    referenceCount = new GoblinReferenceCount(count);
  }

  public void onNext(@Nullable T value) {
    queue.add(value);
  }

  public synchronized void onError(@NotNull Throwable error) {
    if (this.error == null) {
      this.error = error;
      countDown();
    }
  }

  public void onComplete() {
    if (referenceCount.release()) {
      countDown();
    }
  }

  @NotNull
  public List<T> block() {
    try {
      await();
    } catch (InterruptedException ex) {
      throw new GoblinInterruptedException(ex);
    }
    if (error != null) {
      RuntimeException ex = ExceptionUtils.propagate(error);
      ex.addSuppressed(new Exception("#BlockingListPublisher terminated with an error"));
      throw ex;
    }
    return Collections.unmodifiableList(queue);
  }
}
