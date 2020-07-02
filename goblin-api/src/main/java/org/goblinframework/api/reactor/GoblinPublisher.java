package org.goblinframework.api.reactor;

import org.jetbrains.annotations.Nullable;
import org.reactivestreams.Publisher;

/**
 * GOBLIN publisher abstraction.
 *
 * @author Xiaohai Zhang
 * @since Nov 30, 2019
 */
public interface GoblinPublisher<T> extends Publisher<T> {

  /**
   * Block with default subscription until this publisher completed.
   *
   * @return The result published to this publisher.
   */
  @Nullable
  T block();
}
