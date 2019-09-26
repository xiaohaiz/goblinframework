package org.goblinframework.core.conversion.converter;

import org.goblinframework.api.core.Install;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;

import java.util.Calendar;
import java.util.Date;

@Install
final public class CalendarToDateConverter implements Converter<Calendar, Date> {

  @NotNull
  @Override
  public Date convert(@NotNull Calendar source) {
    return source.getTime();
  }
}
