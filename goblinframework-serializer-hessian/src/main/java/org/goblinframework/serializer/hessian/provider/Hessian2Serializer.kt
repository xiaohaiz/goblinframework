package org.goblinframework.serializer.hessian.provider

import org.goblinframework.api.serialization.Serialization
import org.goblinframework.api.serialization.Serializer
import java.io.InputStream
import java.io.OutputStream

class Hessian2Serializer : Serialization {

  override fun getSerializer(): Serializer {
    return Serializer.HESSIAN2
  }

  override fun serialize(obj: Any, outStream: OutputStream) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun deserialize(inStream: InputStream): Any {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }
}