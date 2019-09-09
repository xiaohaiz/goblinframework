package org.goblinframework.core.conversion.converter

import org.springframework.core.convert.converter.Converter

import java.time.Instant

class InstantToLongConverter : Converter<Instant, Long> {

  override fun convert(source: Instant): Long? {
    return source.toEpochMilli()
  }
}
