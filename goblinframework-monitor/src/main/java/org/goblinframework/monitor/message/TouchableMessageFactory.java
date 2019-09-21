package org.goblinframework.monitor.message;

import org.jetbrains.annotations.NotNull;

public interface TouchableMessageFactory<E extends TouchableMessage> {

  @NotNull
  E newInstance();
}
