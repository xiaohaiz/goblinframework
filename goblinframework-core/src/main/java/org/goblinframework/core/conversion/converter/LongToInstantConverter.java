package org.goblinframework.core.conversion.converter;

import org.goblinframework.api.core.Install;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;

import java.time.Instant;

@Install
final public class LongToInstantConverter implements Converter<Long, Instant> {

  @NotNull
  @Override
  public Instant convert(@NotNull Long source) {
    return Instant.ofEpochMilli(source);
  }
}
