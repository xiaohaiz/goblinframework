package org.goblinframework.webmvc.mapping.parameter

import org.goblinframework.webmvc.mapping.NamedParameter
import org.goblinframework.webmvc.mapping.ParameterMapping

class RequestBodyParameterMapping(namedParameter: NamedParameter,
                                  val required: Boolean)
  : ParameterMapping(namedParameter)