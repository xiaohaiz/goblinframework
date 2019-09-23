package org.goblinframework.core.conversion.converter;

import org.goblinframework.api.common.Install;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;

import java.util.Calendar;

@Install
final public class CalendarToLongConverter implements Converter<Calendar, Long> {

  @NotNull
  @Override
  public Long convert(@NotNull Calendar source) {
    return source.getTimeInMillis();
  }
}
