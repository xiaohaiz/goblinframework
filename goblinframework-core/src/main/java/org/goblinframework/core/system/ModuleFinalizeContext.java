package org.goblinframework.core.system;

import org.goblinframework.api.event.GoblinEventListener;
import org.jetbrains.annotations.NotNull;

public interface ModuleFinalizeContext extends ModuleContext {

  void unsubscribeEventLister(@NotNull GoblinEventListener listener);

}
