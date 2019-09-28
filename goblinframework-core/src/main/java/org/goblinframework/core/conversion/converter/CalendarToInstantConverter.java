package org.goblinframework.core.conversion.converter;

import org.goblinframework.api.annotation.Install;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;

import java.time.Instant;
import java.util.Calendar;

@Install
final public class CalendarToInstantConverter implements Converter<Calendar, Instant> {

  @NotNull
  @Override
  public Instant convert(@NotNull Calendar source) {
    return source.toInstant();
  }
}
