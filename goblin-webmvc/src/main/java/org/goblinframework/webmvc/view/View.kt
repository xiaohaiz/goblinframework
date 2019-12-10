package org.goblinframework.webmvc.view

import org.goblinframework.webmvc.servlet.GoblinServletRequest
import org.goblinframework.webmvc.servlet.GoblinServletResponse
import org.springframework.ui.Model

interface View {

  fun render(model: Model?, request: GoblinServletRequest, response: GoblinServletResponse)

}