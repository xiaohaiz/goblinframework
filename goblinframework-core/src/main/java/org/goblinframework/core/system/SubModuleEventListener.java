package org.goblinframework.core.system;

import org.goblinframework.api.common.Singleton;
import org.goblinframework.api.event.GoblinEventChannel;
import org.goblinframework.api.event.GoblinEventContext;
import org.goblinframework.api.event.GoblinEventListener;
import org.goblinframework.api.system.ModuleFinalizeContext;
import org.goblinframework.api.system.ModuleInitializeContext;
import org.goblinframework.api.system.ModuleInstallContext;
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
        logger.info("Install [{}]", e.id().fullName());
        event.ctx.setExtension(e.getClass().getName(), e);
        e.install((ModuleInstallContext) event.ctx);
      });
    } else if (event.ctx instanceof ModuleInitializeContext) {
      event.subModules.forEach(e -> {
        logger.info("Initialize [{}]", e.id().fullName());
        e.initialize((ModuleInitializeContext) event.ctx);
      });
    } else if (event.ctx instanceof ModuleFinalizeContext) {
      event.subModules.forEach(e -> {
        logger.info("Install [{}]", e.id().fullName());
        e.finalize((ModuleFinalizeContext) event.ctx);
      });
    } else {
      throw new UnsupportedOperationException();
    }
  }
}
