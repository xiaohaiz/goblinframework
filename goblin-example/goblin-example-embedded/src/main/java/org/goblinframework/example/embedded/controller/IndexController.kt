package org.goblinframework.example.embedded.controller

import org.goblinframework.example.embedded.utils.AuthUtils
import org.goblinframework.webmvc.servlet.GoblinServletRequest
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.io.Serializable

@Controller
@RequestMapping("/")
class IndexController {

  class ResponseData(val code: Int, val info: String) : Serializable {

    companion object {
      private const val serialVersionUID = -4568367284300289602L
    }

    val content = mutableMapOf<String, Any>()

    fun add(key: String, value: Any): ResponseData {
      content[key] = value
      return this
    }

    fun remove(key: String): ResponseData {
      content.remove(key)
      return this
    }
  }

  @RequestMapping("index.goblin")
  fun index(): String {
    return "index"
  }

  @RequestMapping("postJson.goblin", method = [RequestMethod.POST])
  @ResponseBody
  fun postJson(@RequestBody s: String): String {
    return "ok"
  }

  @RequestMapping("json.goblin")
  @ResponseBody
  fun json(@RequestParam("a") a: Int): ResponseData {
    return ResponseData(200, "success")
        .add("userId", 1)
        .add("a", a)
  }

  @RequestMapping("user/info.goblin")
  @ResponseBody
  fun user(request: GoblinServletRequest): ResponseData {
    val userInfo = request.servletRequest.getAttribute("userInfo") as AuthUtils.UserInfoMapper
    return ResponseData(200, "success")
        .add("name", userInfo.loginName)
  }

  @RequestMapping("key.goblin")
  @ResponseBody
  fun key(): ResponseData {
    val userInfo = AuthUtils.UserInfoMapper("Moltres", "123456")
    return ResponseData(200, "success")
        .add("name", userInfo.loginName)
        .add("password", userInfo.loginPW)
        .add("session_key", AuthUtils.encodeSession(userInfo))
  }
}