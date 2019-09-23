package org.goblinframework.api.common;

import org.goblinframework.api.annotation.External;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

@External
public interface IRandomProvider {

  @NotNull
  Random getRandom();

}
