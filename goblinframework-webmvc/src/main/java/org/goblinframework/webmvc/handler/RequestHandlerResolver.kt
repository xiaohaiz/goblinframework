package org.goblinframework.webmvc.handler

import org.apache.commons.io.IOUtils
import org.goblinframework.core.conversion.ConversionService
import org.goblinframework.core.util.JsonUtils
import org.goblinframework.webmvc.exception.*
import org.goblinframework.webmvc.mapping.parameter.*
import org.goblinframework.webmvc.servlet.ServletRequest
import org.goblinframework.webmvc.servlet.ServletResponse
import org.goblinframework.webmvc.util.DefaultPathMatcher
import org.goblinframework.webmvc.util.DefaultUrlPathHelper
import org.springframework.validation.support.BindingAwareModelMap
import org.springframework.web.multipart.MultipartHttpServletRequest
import java.io.IOException
import java.io.InputStream

object RequestHandlerResolver {

  fun resolve(handler: RequestHandlerImpl,
              request: ServletRequest,
              response: ServletResponse): Array<Any?> {

    val mapping = handler.controllerMapping
    val parameters = mapping.requestHandlerParameters
    if (parameters.isEmpty()) {
      return emptyArray()
    }

    var requestBody: InputStream? = null
    if (mapping.hasRequestBodyParameter()) {
      // @RequestBody annotation presented on one of parameters
      try {
        requestBody = request.body
        if (requestBody == null) {
          if (mapping.getRequestBodyParameter()!!.required) {
            throw RequestBodyMissingException()
          }
        }
      } catch (ex: IOException) {
        throw RequestBodyNotReadableException(ex)
      }
    }

    try {
      return internalResolve(handler, request, response, requestBody)
    } finally {
      requestBody?.close()
    }
  }

  private fun internalResolve(handler: RequestHandlerImpl,
                              request: ServletRequest,
                              response: ServletResponse,
                              requestBody: InputStream?): Array<Any?> {

    val conversionService = ConversionService.INSTANCE
    val pathMatcher = DefaultPathMatcher.INSTANCE
    val urlPathHelper = DefaultUrlPathHelper.INSTANCE

    val mapping = handler.controllerMapping
    val parameters = mapping.requestHandlerParameters

    val decodedUriVariables = mutableMapOf<String, String>()
    if (mapping.hasPathVariableParameter()) {
      val uriVariables = pathMatcher.extractUriTemplateVariables(mapping.requestURL, handler.lookupPath)
      val decodes = urlPathHelper.decodePathVariables(request.servletRequest, uriVariables)
      decodedUriVariables.putAll(decodes)
      val vars = mapping.getPathVariableParameters()
          .map { it.name }
          .toSet()
      for (`var` in vars) {
        if (!decodedUriVariables.containsKey(`var`)) {
          throw PathVariableMissingException()
        }
      }
    }

    val resolved = ArrayList<Any?>(parameters.size)
    for (parameter in parameters) {
      if (parameter is HttpServletRequestParameterMapping) {
        resolved.add(request.servletRequest)
        continue
      }
      if (parameter is HttpServletResponseParameterMapping) {
        resolved.add(response.servletResponse)
        continue
      }
      if (parameter is ServletRequestParameterMapping) {
        resolved.add(request)
        continue
      }
      if (parameter is ServletResponseParameterMapping) {
        resolved.add(response)
        continue
      }
      if (parameter is ServletServerHttpRequestParameterMapping) {
        resolved.add(request)
        continue
      }
      if (parameter is ServletServerHttpResponseParameterMapping) {
        resolved.add(response)
        continue
      }
      if (parameter is ModelParameterMapping) {
        resolved.add(BindingAwareModelMap())
        continue
      }
      if (parameter is MultipartFileParameterMapping) {
        val name = parameter.name
        val servletRequest = request.servletRequest as? MultipartHttpServletRequest
            ?: throw NotMultipartHttpServletRequestException()
        resolved.add(servletRequest.getFile(name))
        continue
      }
      if (parameter is PathVariableParameterMapping) {
        val name = parameter.name
        val value = decodedUriVariables[name]
        try {
          val type = parameter.namedParameter.parameter.type
          resolved.add(conversionService.convert(value, type))
        } catch (ex: Exception) {
          throw PathVariableConversionException(ex)
        }
        continue
      }
      if (parameter is RequestBodyParameterMapping) {
        if (requestBody == null) {
          // it means @RequestBody is optional
          resolved.add(null)
        } else {
          // convert to request body to object (json)
          val type = parameter.namedParameter.genericParameterType
          try {
            if (type === String::class.java) {
              resolved.add(IOUtils.toString(requestBody, Charsets.UTF_8.name()))
            } else {
              val mapper = JsonUtils.getDefaultObjectMapper()
              val javaType = mapper.typeFactory.constructType(type)
              resolved.add(mapper.readValue(requestBody, javaType))
            }
          } catch (ex: Exception) {
            throw RequestBodyConversionException(ex)
          }
        }
        continue
      }
      if (parameter is RequestParamParameterMapping) {
        val name = parameter.name
        val value = request.servletRequest.getParameter(name)
        if (value != null) {
          try {
            val type = parameter.namedParameter.parameter.type
            resolved.add(conversionService.convert(value, type))
          } catch (ex: Exception) {
            throw RequestParamConversionException(ex)
          }
          continue
        }
        if (parameter.hasDefaultValue()) {
          try {
            val type = parameter.namedParameter.parameter.type
            resolved.add(conversionService.convert(parameter.defaultValue, type))
          } catch (ex: Exception) {
            throw RequestParamConversionException(ex)
          }
          continue
        }
        if (!parameter.required) {
          resolved.add(null)
        } else {
          throw RequestParamMissingException()
        }
        continue
      }
      throw UnsupportedOperationException()
    }
    return resolved.toTypedArray()
  }
}