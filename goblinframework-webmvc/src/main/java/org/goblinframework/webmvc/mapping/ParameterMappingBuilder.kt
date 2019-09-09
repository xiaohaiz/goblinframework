package org.goblinframework.webmvc.mapping

import org.goblinframework.core.util.StringUtils
import org.goblinframework.webmvc.mapping.parameter.*
import org.goblinframework.webmvc.servlet.GoblinServletServerHttpRequest
import org.goblinframework.webmvc.servlet.GoblinServletServerHttpResponse
import org.springframework.http.server.ServletServerHttpRequest
import org.springframework.http.server.ServletServerHttpResponse
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ValueConstants
import org.springframework.web.multipart.MultipartFile
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

object ParameterMappingBuilder {

  fun build(namedParameter: NamedParameter): ParameterMapping {
    val t = namedParameter.parameter.type
    if (Model::class.java.isAssignableFrom(t)) {
      return ModelParameterMapping(namedParameter)
    }
    if (HttpServletRequest::class.java.isAssignableFrom(t)) {
      return HttpServletRequestParameterMapping(namedParameter)
    }
    if (HttpServletResponse::class.java.isAssignableFrom(t)) {
      return HttpServletResponseParameterMapping(namedParameter)
    }
    if (ServletServerHttpRequest::class.java.isAssignableFrom(t)) {
      return ServletServerHttpRequestParameterMapping(namedParameter)
    }
    if (ServletServerHttpResponse::class.java.isAssignableFrom(t)) {
      return ServletServerHttpResponseParameterMapping(namedParameter)
    }
    if (GoblinServletServerHttpRequest::class.java.isAssignableFrom(t)) {
      return GoblinServletServerHttpRequestParameterMapping(namedParameter)
    }
    if (GoblinServletServerHttpResponse::class.java.isAssignableFrom(t)) {
      return GoblinServletServerHttpResponseParameterMapping(namedParameter)
    }
    if (MultipartFile::class.java.isAssignableFrom(t)) {
      var name = namedParameter.name
      if (namedParameter.parameter.isAnnotationPresent(RequestParam::class.java)) {
        val requestParam = namedParameter.parameter.getAnnotation(RequestParam::class.java)
        val n = extractNameFromRequestParam(requestParam)
        if (n != null) {
          name = n
        }
      }
      return MultipartFileParameterMapping(namedParameter, name)
    }
    if (namedParameter.parameter.isAnnotationPresent(RequestBody::class.java)) {
      val requestBody = namedParameter.parameter.getAnnotation(RequestBody::class.java)
      return RequestBodyParameterMapping(namedParameter, requestBody.required)
    }
    if (namedParameter.parameter.isAnnotationPresent(PathVariable::class.java)) {
      var name = namedParameter.name
      val pathVariable = namedParameter.parameter.getAnnotation(PathVariable::class.java)
      if (StringUtils.isNotBlank(pathVariable.value)) {
        name = StringUtils.trim(pathVariable.value)
      }
      return PathVariableParameterMapping(namedParameter, name)
    } else {
      var name = namedParameter.name
      var required = true
      var defaultValue = ValueConstants.DEFAULT_NONE
      if (namedParameter.parameter.isAnnotationPresent(RequestParam::class.java)) {
        val requestParam = namedParameter.parameter.getAnnotation(RequestParam::class.java)
        val n = extractNameFromRequestParam(requestParam)
        if (n != null) {
          name = n
        }
        required = requestParam.required
        defaultValue = requestParam.defaultValue
      }
      return RequestParamParameterMapping(namedParameter, name, required, defaultValue)
    }
  }

  private fun extractNameFromRequestParam(requestParam: RequestParam): String? {
    if (StringUtils.isNotBlank(requestParam.value)) {
      return StringUtils.trim(requestParam.value)
    }
    return if (StringUtils.isNotBlank(requestParam.name)) {
      StringUtils.trim(requestParam.name)
    } else null
  }
}