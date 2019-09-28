package org.goblinframework.core.conversion.converter;

import org.goblinframework.api.annotation.Install;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;

import java.util.Calendar;
import java.util.Date;

@Install
final public class DateToCalendarConverter implements Converter<Date, Calendar> {

  @NotNull
  @Override
  public Calendar convert(@NotNull Date source) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(source);
    return calendar;
  }
}
