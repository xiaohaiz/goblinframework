package org.goblinframework.core.conversion.converter;

import org.goblinframework.api.annotation.Install;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;

import java.time.Instant;

@Install
public class LongToInstantConverter implements Converter<Long, Instant> {

  @Override
  public Instant convert(@NotNull Long source) {
    return Instant.ofEpochMilli(source);
  }
}
