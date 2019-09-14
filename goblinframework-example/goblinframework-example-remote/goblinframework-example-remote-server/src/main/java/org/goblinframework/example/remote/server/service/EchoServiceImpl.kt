package org.goblinframework.example.remote.server.service

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.example.remote.api.EchoService

@Singleton
class EchoServiceImpl private constructor() : EchoService {

  companion object {
    @JvmField val INSTANCE = EchoServiceImpl()
  }

  override fun echo(message: String?): String? {
    return message
  }
}