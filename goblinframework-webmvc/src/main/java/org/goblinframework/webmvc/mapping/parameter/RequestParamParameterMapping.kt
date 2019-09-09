package org.goblinframework.webmvc.mapping.parameter

import org.springframework.web.bind.annotation.ValueConstants

class RequestParamParameterMapping(namedParameter: NamedParameter,
                                   val name: String,
                                   val required: Boolean,
                                   val defaultValue: String)
  : ParameterMapping(namedParameter) {

  fun hasDefaultValue(): Boolean {
    return ValueConstants.DEFAULT_NONE != defaultValue
  }
}