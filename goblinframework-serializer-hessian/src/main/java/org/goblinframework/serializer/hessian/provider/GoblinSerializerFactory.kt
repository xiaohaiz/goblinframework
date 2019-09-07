package org.goblinframework.serializer.hessian.provider

import com.caucho.hessian.io.SerializerFactory
import org.goblinframework.core.util.ClassUtils

class GoblinSerializerFactory private constructor()
  : SerializerFactory(ClassUtils.getDefaultClassLoader()) {

  companion object {
    @JvmField val INSTANCE = GoblinSerializerFactory()
  }

}