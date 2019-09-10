package org.goblinframework.webmvc.view

import org.goblinframework.webmvc.servlet.ServletRequest
import org.goblinframework.webmvc.servlet.ServletResponse
import org.springframework.ui.Model

interface View {

  fun render(model: Model?, request: ServletRequest, response: ServletResponse)

}