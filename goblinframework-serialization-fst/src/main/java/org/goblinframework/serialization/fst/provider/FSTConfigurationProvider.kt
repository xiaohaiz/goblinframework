package org.goblinframework.serialization.fst.provider

import org.nustaq.serialization.FSTConfiguration

object FSTConfigurationProvider {

  val configuration = FSTConfiguration.createDefaultConfiguration().also {
    it.isShareReferences = false
  }

}