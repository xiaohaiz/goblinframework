package org.goblinframework.core.conversion.converter;

import org.goblinframework.api.common.Install;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;

import java.time.Instant;
import java.util.Calendar;

@Install
final public class InstantToCalendarConverter implements Converter<Instant, Calendar> {

  @NotNull
  @Override
  public Calendar convert(@NotNull Instant source) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(source.toEpochMilli());
    return calendar;
  }
}
