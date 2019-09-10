package org.goblinframework.webmvc.view

import org.goblinframework.api.common.Ordered

interface ViewResolver : Ordered {

  fun resolve(name: String?): View?

}