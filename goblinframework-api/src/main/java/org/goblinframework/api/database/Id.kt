package org.goblinframework.api.database

@MustBeDocumented
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class Id(val value: Generator) {

  enum class Generator {
    NONE,
    OBJECT_ID,
    AUTO_INC
  }
}
