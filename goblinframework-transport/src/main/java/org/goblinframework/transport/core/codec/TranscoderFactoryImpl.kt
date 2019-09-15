package org.goblinframework.transport.core.codec

import org.goblinframework.api.annotation.Install
import org.goblinframework.api.annotation.Singleton
import org.goblinframework.core.transcoder.Transcoder
import org.goblinframework.core.transcoder.TranscoderFactory
import org.goblinframework.core.transcoder.TranscoderSetting

@Singleton
class TranscoderFactoryImpl private constructor() : TranscoderFactory {

  companion object {
    @JvmField val INSTANCE = TranscoderFactoryImpl()
  }

  override fun buildTranscoder(setting: TranscoderSetting): Transcoder {
    return TranscoderImpl(setting)
  }

  @Install
  class Installer : TranscoderFactory by INSTANCE
}
