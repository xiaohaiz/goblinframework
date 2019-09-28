package org.goblinframework.registry.core.manager;

import org.goblinframework.registry.core.Registry;
import org.goblinframework.registry.core.RegistryLocation;
import org.goblinframework.registry.core.RegistryPathListener;
import org.goblinframework.registry.core.RegistryPathWatchdog;
import org.jetbrains.annotations.NotNull;

abstract public class AbstractRegistry implements Registry {

  private final RegistryLocation location;

  protected AbstractRegistry(@NotNull RegistryLocation location) {
    this.location = location;
  }

  @NotNull
  @Override
  public RegistryLocation location() {
    return location;
  }

  @NotNull
  @Override
  public RegistryPathListener createPathListener() {
    return new RegistryPathListenerImpl(this);
  }

  @NotNull
  @Override
  public RegistryPathWatchdog createPathWatchdog() {
    return new RegistryPathWatchdogImpl(this);
  }
}
