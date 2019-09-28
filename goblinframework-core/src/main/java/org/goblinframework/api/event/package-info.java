package org.goblinframework.api.event;

import static org.goblinframework.api.core.ServiceInstaller.firstOrNull;

final class EventBusManagerInstaller {
  static final IEventBusManager INSTALLED = firstOrNull(IEventBusManager.class);
}