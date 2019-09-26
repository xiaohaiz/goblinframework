package org.goblinframework.registry.core.manager;

import org.goblinframework.api.common.TimeAndUnit;
import org.goblinframework.api.registry.Registry;
import org.goblinframework.api.registry.RegistryLocation;
import org.goblinframework.api.registry.RegistryPathListener;
import org.goblinframework.api.registry.RegistryPathWatchdog;
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
  public RegistryPathWatchdog createPathWatchdog(@NotNull TimeAndUnit period) {
    return new RegistryPathWatchdogImpl(this, period);
  }
}
