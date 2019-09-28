package org.goblinframework.core.conversion.converter;

import org.goblinframework.api.annotation.Install;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;

import java.time.Instant;

@Install
final public class InstantToLongConverter implements Converter<Instant, Long> {

  @NotNull
  @Override
  public Long convert(@NotNull Instant source) {
    return source.toEpochMilli();
  }
}
