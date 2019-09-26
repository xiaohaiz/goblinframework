package org.goblinframework.webmvc.view

import org.goblinframework.api.core.Ordered

interface ViewResolver : Ordered {

  fun resolve(name: String?): View?

}