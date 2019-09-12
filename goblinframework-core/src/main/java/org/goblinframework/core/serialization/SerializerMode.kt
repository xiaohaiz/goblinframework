package org.goblinframework.core.serialization

enum class SerializerMode constructor(val id: Byte) {

  JAVA(1.toByte()),
  FST(2.toByte()),
  HESSIAN2(3.toByte());

  companion object {
    private val modes = mutableMapOf<Byte, SerializerMode>()

    init {
      values().forEach { modes[it.id] = it }
    }

    fun parse(id: Byte): SerializerMode? {
      return modes[id]
    }
  }

}
