package org.goblinframework.core.serialization.hessian

import com.caucho.hessian.io.HessianFactory
import com.caucho.hessian.io.SerializerFactory
import org.goblinframework.api.annotation.Singleton
import org.goblinframework.core.util.getDefaultClassLoader

@Singleton
class GoblinHessianFactory private constructor() : HessianFactory() {

  companion object {
    @JvmField internal val INSTANCE = GoblinHessianFactory()
  }

  init {
    serializerFactory = SerializerFactory(getDefaultClassLoader())
  }
}

/**
 * Returns the unique [HessianFactory] instance.
 */
fun getHessianFactory(): HessianFactory {
  return GoblinHessianFactory.INSTANCE
}