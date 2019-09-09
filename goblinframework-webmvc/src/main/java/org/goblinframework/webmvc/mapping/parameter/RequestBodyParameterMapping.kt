package org.goblinframework.webmvc.mapping.parameter

class RequestBodyParameterMapping(namedParameter: NamedParameter,
                                  val required: Boolean)
  : ParameterMapping(namedParameter)