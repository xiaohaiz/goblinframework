package org.goblinframework.api.dao

@MustBeDocumented
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class GoblinId(val value: Generator) {

  enum class Generator {
    NONE,
    OBJECT_ID,
    AUTO_INC
  }
}
