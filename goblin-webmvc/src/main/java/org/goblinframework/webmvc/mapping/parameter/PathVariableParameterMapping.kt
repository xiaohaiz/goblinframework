package org.goblinframework.webmvc.mapping.parameter

class PathVariableParameterMapping(namedParameter: NamedParameter,
                                   val name: String)
  : ParameterMapping(namedParameter)