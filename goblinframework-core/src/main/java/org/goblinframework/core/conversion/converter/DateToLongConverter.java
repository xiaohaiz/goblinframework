package org.goblinframework.core.conversion.converter;

import org.goblinframework.api.core.Install;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;

import java.util.Date;

@Install
final public class DateToLongConverter implements Converter<Date, Long> {

  @NotNull
  @Override
  public Long convert(@NotNull Date source) {
    return source.getTime();
  }
}
