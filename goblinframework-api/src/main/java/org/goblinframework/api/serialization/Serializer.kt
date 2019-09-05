package org.goblinframework.api.serialization

enum class Serializer(val id: Byte) {

  JAVA(0.toByte()),
  HESSIAN2(1.toByte())
}
