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

    fun resolve(input: Any?): SerializerMode? {
      if (input == null) {
        return null
      }
      if (input is SerializerMode) {
        return input
      }
      if (input is Number) {
        return modes[input.toByte()]
      }
      if (input is String) {
        return try {
          valueOf(input)
        } catch (ex: Exception) {
          try {
            modes[input.toByte()]
          } catch (e: Exception) {
            null
          }
        }
      }
      return null
    }

    fun parse(id: Byte): SerializerMode? {
      return modes[id]
    }
  }

}
