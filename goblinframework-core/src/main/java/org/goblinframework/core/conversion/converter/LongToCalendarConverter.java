package org.goblinframework.core.conversion.converter;

import org.goblinframework.api.annotation.Install;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;

import java.util.Calendar;

@Install
final public class LongToCalendarConverter implements Converter<Long, Calendar> {

  @NotNull
  @Override
  public Calendar convert(@NotNull Long source) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(source);
    return calendar;
  }
}
