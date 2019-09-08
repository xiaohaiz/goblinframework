package org.goblinframework.serialization.fst.provider

import org.nustaq.serialization.FSTConfiguration

object FstConfigurationFactory {

  val configuration: FSTConfiguration = FSTConfiguration.createDefaultConfiguration().also {
    it.isShareReferences = false
  }

}