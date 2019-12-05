package org.goblinframework.webmvc.module.converter

import org.springframework.core.convert.converter.Converter
import org.springframework.http.HttpMethod
import org.springframework.web.bind.annotation.RequestMethod

class HttpMethodToRequestMethodConverter : Converter<HttpMethod, RequestMethod> {

  override fun convert(source: HttpMethod): RequestMethod? {
    return try {
      RequestMethod.valueOf(source.name)
    } catch (e: Exception) {
      null
    }
  }
}