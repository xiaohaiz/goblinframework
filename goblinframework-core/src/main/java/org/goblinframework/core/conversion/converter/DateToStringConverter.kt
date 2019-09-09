package org.goblinframework.core.conversion.converter

import org.goblinframework.core.util.DateFormatUtils
import org.springframework.core.convert.converter.Converter
import java.util.*

class DateToStringConverter : Converter<Date, String> {

  override fun convert(source: Date): String? {
    return DateFormatUtils.format(source)
  }
}