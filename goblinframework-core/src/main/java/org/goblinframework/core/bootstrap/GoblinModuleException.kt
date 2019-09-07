package org.goblinframework.core.bootstrap

class GoblinModuleException : RuntimeException {

  constructor() {}

  constructor(message: String) : super(message) {}

  constructor(message: String, cause: Throwable) : super(message, cause) {}

  constructor(cause: Throwable) : super(cause) {}

  companion object {
    private const val serialVersionUID = -528572139800126784L
  }
}
