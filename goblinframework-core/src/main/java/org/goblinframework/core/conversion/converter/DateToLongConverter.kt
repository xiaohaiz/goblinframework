package org.goblinframework.core.conversion.converter

import org.springframework.core.convert.converter.Converter
import java.util.*

class DateToLongConverter : Converter<Date, Long> {

  override fun convert(source: Date): Long? {
    return source.time
  }
}
