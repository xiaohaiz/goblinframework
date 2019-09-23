package org.goblinframework.core.system;

import org.goblinframework.api.annotation.Singleton;
import org.goblinframework.api.system.ModuleFinalizeContext;
import org.goblinframework.api.system.ModuleInitializeContext;
import org.goblinframework.api.system.ModuleInstallContext;
import org.goblinframework.core.event.GoblinEventChannel;
import org.goblinframework.core.event.GoblinEventContext;
import org.goblinframework.core.event.GoblinEventListener;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
@GoblinEventChannel("/goblin/core")
final class SubModuleEventListener implements GoblinEventListener {
  private static final Logger logger = LoggerFactory.getLogger(SubModuleEventListener.class);

  static final SubModuleEventListener INSTANCE = new SubModuleEventListener();

  private SubModuleEventListener() {
  }

  @Override
  public boolean accept(@NotNull GoblinEventContext context) {
    return context.getEvent() instanceof SubModuleEvent;
  }

  @Override
  public void onEvent(@NotNull GoblinEventContext context) {
    SubModuleEvent event = (SubModuleEvent) context.getEvent();
    if (event.ctx instanceof ModuleInstallContext) {
      event.subModules.forEach(e -> {
        logger.info("Install [{}]", e.name());
        e.install((ModuleInstallContext) event.ctx);
      });
    } else if (event.ctx instanceof ModuleInitializeContext) {
      event.subModules.forEach(e -> {
        logger.info("Initialize [{}]", e.name());
        e.initialize((ModuleInitializeContext) event.ctx);
      });
    } else if (event.ctx instanceof ModuleFinalizeContext) {
      event.subModules.forEach(e -> {
        logger.info("Install [{}]", e.name());
        e.finalize((ModuleFinalizeContext) event.ctx);
      });
    } else {
      throw new UnsupportedOperationException();
    }
  }
}
