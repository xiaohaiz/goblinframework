package org.goblinframework.core.conversion.converter

import org.goblinframework.core.util.DateFormatUtils
import org.springframework.core.convert.converter.Converter
import java.util.*

class CalendarToStringConverter : Converter<Calendar, String> {

  override fun convert(source: Calendar): String? {
    return DateFormatUtils.format(source)
  }
}