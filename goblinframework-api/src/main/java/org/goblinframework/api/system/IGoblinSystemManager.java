package org.goblinframework.api.system;

import org.goblinframework.api.annotation.Internal;
import org.goblinframework.api.annotation.SPI;
import org.goblinframework.api.common.Lifecycle;
import org.goblinframework.api.service.ServiceInstaller;
import org.jetbrains.annotations.NotNull;

@SPI
@Internal
public interface IGoblinSystemManager extends Lifecycle {

  @NotNull
  static IGoblinSystemManager instance() {
    IGoblinSystemManager system = ServiceInstaller.firstOrNull(IGoblinSystemManager.class);
    if (system == null) {
      throw new GoblinSystemException("No IGoblinSystem installed");
    }
    return system;
  }
}
