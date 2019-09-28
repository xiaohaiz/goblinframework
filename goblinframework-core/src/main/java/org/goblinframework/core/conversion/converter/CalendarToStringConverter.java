package org.goblinframework.core.conversion.converter;

import org.apache.commons.lang3.time.FastDateFormat;
import org.goblinframework.api.annotation.Install;
import org.goblinframework.core.util.DateFormatUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;

import java.util.Calendar;

@Install
final public class CalendarToStringConverter implements Converter<Calendar, String> {

  @NotNull
  @Override
  public String convert(@NotNull Calendar source) {
    FastDateFormat formatter = DateFormatUtils.getDefaultFormatter();
    return formatter.format(source);
  }
}
