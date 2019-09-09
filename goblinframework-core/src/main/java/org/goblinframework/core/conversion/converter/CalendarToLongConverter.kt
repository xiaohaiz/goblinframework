package org.goblinframework.core.conversion.converter

import org.springframework.core.convert.converter.Converter
import java.util.*

class CalendarToLongConverter : Converter<Calendar, Long> {

  override fun convert(source: Calendar): Long? {
    return source.timeInMillis
  }
}
