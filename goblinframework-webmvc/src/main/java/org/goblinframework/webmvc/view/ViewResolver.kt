package org.goblinframework.webmvc.view

import org.goblinframework.api.function.Ordered

interface ViewResolver : Ordered {

  fun resolve(name: String?): View?

}