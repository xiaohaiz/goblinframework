package org.goblinframework.webmvc.module.converter

import org.springframework.core.convert.converter.Converter
import org.springframework.http.HttpMethod
import org.springframework.web.bind.annotation.RequestMethod

class RequestMethodToHttpMethodConverter : Converter<RequestMethod, HttpMethod> {

  override fun convert(source: RequestMethod): HttpMethod? {
    return HttpMethod.resolve(source.name)
  }
}