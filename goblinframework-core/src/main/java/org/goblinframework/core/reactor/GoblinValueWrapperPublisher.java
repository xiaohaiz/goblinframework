package org.goblinframework.core.reactor;

import org.goblinframework.api.function.ValueWrapper;
import org.goblinframework.api.reactor.GoblinPublisher;
import org.jetbrains.annotations.Nullable;
import org.reactivestreams.Subscriber;

import java.io.Serializable;

/**
 * GOBLIN value wrapper publisher implementation for remote transport purpose.
 *
 * @author Xiaohai Zhang
 * @serial
 * @since Nov 30, 2019
 */
public class GoblinValueWrapperPublisher<T> implements GoblinPublisher<T>, Serializable, ValueWrapper<T> {
  private static final long serialVersionUID = -1149466283948305844L;

  @Nullable private final T value;

  public GoblinValueWrapperPublisher(@Nullable T value) {
    this.value = value;
  }

  @Nullable
  @Override
  public T getValue() {
    return value;
  }

  @Override
  public void subscribe(Subscriber<? super T> s) {
    SingleResultPublisher<T> publisher = new SingleResultPublisher<>(CoreScheduler.INSTANCE.get());
    publisher.complete(value, null);
    publisher.subscribe(s);
  }

  @Nullable
  @Override
  public T block() {
    return value;
  }
}
