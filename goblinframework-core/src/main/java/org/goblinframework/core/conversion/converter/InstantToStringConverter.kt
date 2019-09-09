package org.goblinframework.core.conversion.converter

import org.goblinframework.core.util.DateFormatUtils
import org.springframework.core.convert.converter.Converter
import java.time.Instant
import java.util.*

class InstantToStringConverter : Converter<Instant, String> {

  override fun convert(source: Instant): String? {
    return DateFormatUtils.format(Date.from(source))
  }
}