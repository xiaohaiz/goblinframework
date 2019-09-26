package org.goblinframework.core.conversion.converter;

import org.goblinframework.api.core.Install;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;

import java.time.Instant;
import java.util.Date;

@Install
final public class InstantToDateConverter implements Converter<Instant, Date> {

  @NotNull
  @Override
  public Date convert(@NotNull Instant source) {
    return Date.from(source);
  }
}
