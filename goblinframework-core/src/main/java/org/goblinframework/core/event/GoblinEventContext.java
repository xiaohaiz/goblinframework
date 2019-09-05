package org.goblinframework.core.event;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface GoblinEventContext {

  @NotNull
  String getChannel();

  @NotNull
  GoblinEvent getEvent();

  boolean isSuccess();

  @NotNull
  Map<String, Object> getExtensions();

}
