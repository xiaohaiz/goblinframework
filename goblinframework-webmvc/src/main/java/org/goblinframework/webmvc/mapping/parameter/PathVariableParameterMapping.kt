package org.goblinframework.webmvc.mapping.parameter

import org.goblinframework.webmvc.mapping.NamedParameter
import org.goblinframework.webmvc.mapping.ParameterMapping

class PathVariableParameterMapping(namedParameter: NamedParameter,
                                   val name: String)
  : ParameterMapping(namedParameter)