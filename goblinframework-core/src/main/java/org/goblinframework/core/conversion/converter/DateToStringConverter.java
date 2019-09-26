package org.goblinframework.core.conversion.converter;

import org.apache.commons.lang3.time.FastDateFormat;
import org.goblinframework.api.core.Install;
import org.goblinframework.core.util.DateFormatUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;

import java.util.Date;

@Install
final public class DateToStringConverter implements Converter<Date, String> {

  @NotNull
  @Override
  public String convert(@NotNull Date source) {
    FastDateFormat formatter = DateFormatUtils.getDefaultFormatter();
    return formatter.format(source);
  }
}
