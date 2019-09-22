package org.goblinframework.api.system;

import org.goblinframework.api.annotation.Internal;
import org.goblinframework.api.annotation.SPI;

@SPI
@Internal
public interface IGoblinSystem {

  void install();

  void uninstall();

}
