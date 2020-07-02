package org.goblinframework.core.serialization.fst

import org.nustaq.serialization.FSTConfiguration

object FstConfigurationFactory {

  val configuration: FSTConfiguration = FSTConfiguration.createDefaultConfiguration().also {
    it.isShareReferences = false
  }

}