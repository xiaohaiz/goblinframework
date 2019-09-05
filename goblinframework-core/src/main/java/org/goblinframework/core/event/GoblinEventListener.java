package org.goblinframework.core.event;

import java.util.EventListener;

public interface GoblinEventListener extends EventListener {

  boolean accept(GoblinEventContext context);

  void onEvent(GoblinEventContext context);

}
