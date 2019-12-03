package org.goblinframework.core.event;

import org.goblinframework.api.concurrent.GoblinFuture;
import org.goblinframework.api.function.Block0;
import org.jetbrains.annotations.NotNull;

/**
 * Goblin event future abstraction.
 *
 * @author Xiaohai Zhang
 * @since Dec 3, 2019
 */
public interface GoblinEventFuture extends GoblinFuture<GoblinEventContext> {

  /**
   * Callback the specified action in case of event future discarded.
   */
  void addDiscardListener(@NotNull Block0 action);
}
