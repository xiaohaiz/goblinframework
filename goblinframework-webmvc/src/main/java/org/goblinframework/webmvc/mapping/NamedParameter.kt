package org.goblinframework.webmvc.mapping

import java.lang.reflect.Parameter
import java.lang.reflect.Type

class NamedParameter(val parameter: Parameter,
                     val genericParameterType: Type,
                     val name: String)