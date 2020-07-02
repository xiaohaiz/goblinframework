package org.goblinframework.core.reactor;

import org.goblinframework.api.function.ValueWrapper;
import org.goblinframework.api.reactor.GoblinPublisher;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

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
  public void subscribe(@NotNull Subscriber<? super T> s) {
    s.onSubscribe(new Subscription() {
      @SuppressWarnings("ConstantConditions")
      @Override
      public void request(long n) {
        if (n > 0) {
          s.onNext(value);
          s.onComplete();
        }
      }

      @Override
      public void cancel() {
      }
    });
  }

  @Nullable
  @Override
  public T block() {
    return value;
  }
}
