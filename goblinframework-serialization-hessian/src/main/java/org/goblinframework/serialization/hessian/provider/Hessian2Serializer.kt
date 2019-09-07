package org.goblinframework.serialization.hessian.provider

import com.caucho.hessian.io.Hessian2Input
import com.caucho.hessian.io.Hessian2Output
import org.goblinframework.serialization.core.Serializer
import java.io.InputStream
import java.io.OutputStream

class Hessian2Serializer : Serializer {

  override fun id(): Byte {
    return Serializer.HESSIAN2
  }

  override fun serialize(obj: Any, outStream: OutputStream) {
    var ho: Hessian2Output? = null
    try {
      ho = GoblinHessianFactory.INSTANCE.createHessian2Output(outStream)
      ho.writeObject(obj)
      ho.flush()
    } finally {
      GoblinHessianFactory.INSTANCE.freeHessian2Output(ho)
    }
  }

  override fun deserialize(inStream: InputStream): Any {
    var hi: Hessian2Input? = null
    try {
      hi = GoblinHessianFactory.INSTANCE.createHessian2Input(inStream)
      return hi.readObject()
    } finally {
      GoblinHessianFactory.INSTANCE.freeHessian2Input(hi)
    }
  }
}