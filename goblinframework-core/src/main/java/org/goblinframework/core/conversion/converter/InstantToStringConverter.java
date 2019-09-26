package org.goblinframework.core.conversion.converter;

import org.apache.commons.lang3.time.FastDateFormat;
import org.goblinframework.api.core.Install;
import org.goblinframework.core.util.DateFormatUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;

import java.time.Instant;
import java.util.Date;

@Install
final public class InstantToStringConverter implements Converter<Instant, String> {

  @NotNull
  @Override
  public String convert(@NotNull Instant source) {
    FastDateFormat formatter = DateFormatUtils.getDefaultFormatter();
    return formatter.format(Date.from(source));
  }
}
