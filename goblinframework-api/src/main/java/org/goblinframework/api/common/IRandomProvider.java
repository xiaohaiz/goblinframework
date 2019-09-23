package org.goblinframework.api.common;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

@External
public interface IRandomProvider {

  @NotNull
  Random getRandom();

}
