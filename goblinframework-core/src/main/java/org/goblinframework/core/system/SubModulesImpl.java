package org.goblinframework.core.system;

import org.goblinframework.api.event.EventBus;
import org.goblinframework.api.event.GoblinEventFuture;
import org.goblinframework.api.system.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.LinkedMultiValueMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

final class SubModulesImpl implements SubModules {

  private final AtomicInteger stage = new AtomicInteger();
  private final LinkedMultiValueMap<Integer, List<GoblinSubModule>> modules = new LinkedMultiValueMap<>();

  @NotNull
  @Override
  public SubModules next() {
    stage.incrementAndGet();
    return this;
  }

  @NotNull
  @Override
  public SubModules module(@NotNull GoblinSubModule... ids) {
    if (ids.length > 0) {
      modules.add(stage.get(), Arrays.stream(ids).collect(Collectors.toList()));
    }
    return this;
  }

  @Override
  public void install(@NotNull ModuleInstallContext ctx) {
    execute(ctx);
  }

  @Override
  public void initialize(@NotNull ModuleInitializeContext ctx) {
    execute(ctx);
  }

  @Override
  public void finalize(@NotNull ModuleFinalizeContext ctx) {
    execute(ctx);
  }

  private void execute(ModuleContext ctx) {
    for (int i = 0; i <= stage.get(); i++) {
      List<List<GoblinSubModule>> idsList = modules.get(i);
      if (idsList == null) {
        continue;
      }
      List<GoblinEventFuture> futures = new ArrayList<>();
      for (List<GoblinSubModule> ids : idsList) {
        List<ISubModule> subModules = ids.stream()
            .map(SubModuleLoader::subModule)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
        if (subModules.isEmpty()) {
          continue;
        }
        SubModuleEvent event = new SubModuleEvent(ctx, subModules);
        futures.add(EventBus.publish(event));
      }
      futures.forEach(e -> {
        try {
          e.awaitUninterruptibly();
        } catch (Exception ex) {
          throw new GoblinSystemException(ex);
        }
      });
    }
  }
}
