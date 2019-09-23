package org.goblinframework.core.conversion.converter;

import org.goblinframework.api.common.Install;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;

import java.time.Instant;
import java.util.Date;

@Install
final public class DateToInstantConverter implements Converter<Date, Instant> {

  @NotNull
  @Override
  public Instant convert(@NotNull Date source) {
    return Instant.ofEpochMilli(source.getTime());
  }
}
