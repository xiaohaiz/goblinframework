package org.goblinframework.core.serialization.hessian.serializer

import com.caucho.hessian.io.AbstractHessianOutput
import com.caucho.hessian.io.AbstractSerializer
import java.time.Instant

class InstantSerializer : AbstractSerializer() {

  override fun writeObject(obj: Any?, out: AbstractHessianOutput) {
    if (obj == null) {
      out.writeNull()
      return
    }
    if (out.addRef(obj)) {
      return
    }
    val cl = obj.javaClass
    val ref = out.writeObjectBegin(cl.name)
    if (ref < -1) {
      out.writeString("value")
      out.writeString((obj as Instant).toEpochMilli().toString())
      out.writeMapEnd()
    } else {
      if (ref == -1) {
        out.writeInt(1)
        out.writeString("value")
        out.writeObjectBegin(cl.name)
      }
      out.writeString((obj as Instant).toEpochMilli().toString())
    }
  }
}
