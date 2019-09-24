package org.goblinframework.registry.core.manager;

import org.goblinframework.api.registry.Registry;
import org.goblinframework.api.registry.RegistryLocation;
import org.goblinframework.api.registry.RegistryPathListener;
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
}
