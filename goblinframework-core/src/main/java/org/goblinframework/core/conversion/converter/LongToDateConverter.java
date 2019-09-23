package org.goblinframework.core.conversion.converter;

import org.goblinframework.api.common.Install;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;

import java.util.Date;

@Install
final public class LongToDateConverter implements Converter<Long, Date> {

  @NotNull
  @Override
  public Date convert(@NotNull Long source) {
    return new Date(source);
  }
}
