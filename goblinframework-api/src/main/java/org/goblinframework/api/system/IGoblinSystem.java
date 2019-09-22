package org.goblinframework.api.system;

import org.goblinframework.api.annotation.Internal;
import org.goblinframework.api.annotation.SPI;
import org.goblinframework.api.service.IServiceInstaller;
import org.jetbrains.annotations.NotNull;

@SPI
@Internal
public interface IGoblinSystem {

  void install();

  void uninstall();

  @NotNull
  static IGoblinSystem instance() {
    IGoblinSystem system = IServiceInstaller.instance().firstOrNull(IGoblinSystem.class);
    if (system == null) {
      throw new GoblinSystemException("No IGoblinSystem installed");
    }
    return system;
  }
}
