package org.goblinframework.core.config

import org.goblinframework.core.util.StringUtils
import java.net.URL

class ConfigLocation(val path: String, val url: URL) {

  fun filename(): String {
    return StringUtils.substringAfterLast(path, "/")
  }

}