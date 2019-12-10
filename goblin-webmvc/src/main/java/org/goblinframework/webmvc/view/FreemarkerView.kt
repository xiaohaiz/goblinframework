package org.goblinframework.webmvc.view

import freemarker.template.Template
import org.goblinframework.webmvc.servlet.GoblinServletRequest
import org.goblinframework.webmvc.servlet.GoblinServletResponse
import org.springframework.http.MediaType
import org.springframework.ui.Model
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils
import org.springframework.validation.support.BindingAwareModelMap
import java.nio.charset.Charset

class FreemarkerView(private val template: Template,
                     private val contentType: MediaType,
                     private val encoding: String) : View {

  override fun render(model: Model?, request: GoblinServletRequest, response: GoblinServletResponse) {
    val m = model ?: BindingAwareModelMap()
    val html = FreeMarkerTemplateUtils.processTemplateIntoString(template, m)
    val bytes = html.toByteArray(Charset.forName(encoding))
    response.headers.contentType = contentType
    response.headers.contentLength = bytes.size.toLong()
    response.body.write(bytes)
  }
}