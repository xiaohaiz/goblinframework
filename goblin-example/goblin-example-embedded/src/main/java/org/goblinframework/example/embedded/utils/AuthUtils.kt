package org.goblinframework.example.embedded.utils

import org.goblinframework.core.cipher.AesCipherUtils
import org.goblinframework.core.config.ConfigManager

/**
 * 验证相关utils
 */
class AuthUtils private constructor() {
  companion object {
    private val COOKIE_AUTH_KEY = ConfigManager.INSTANCE.getConfig("embedded", "cookie_secret_key")
    private val SESSION_AUTH_KEY = ConfigManager.INSTANCE.getConfig("embedded", "session_secret_key")

    fun encodeCookie(userInfoMapper: UserInfoMapper): String {
      val value = "${userInfoMapper.loginName}:${userInfoMapper.loginPW}"
      return AesCipherUtils.encryptBase64String(COOKIE_AUTH_KEY, value);
    }

    fun decodeCookie(cookie: String): UserInfoMapper? {
      val value = AesCipherUtils.decryptBase64String(COOKIE_AUTH_KEY, cookie)
          ?: return null

      val data = value.split(":")
      if (data.size != 2) {
        return null
      }

      return UserInfoMapper(data[0], data[1])
    }

    fun encodeSession(userInfoMapper: UserInfoMapper): String {
      val value = "${userInfoMapper.loginName}:${userInfoMapper.loginPW}"
      return AesCipherUtils.encryptBase64String(SESSION_AUTH_KEY, value);
    }

    fun decodeSession(session: String): UserInfoMapper? {
      val value = AesCipherUtils.decryptBase64String(SESSION_AUTH_KEY, session)
          ?: return null

      val data = value.split(":")
      if (data.size != 2) {
        return null
      }

      return UserInfoMapper(data[0], data[1])
    }
  }

  class UserInfoMapper(val loginName: String, val loginPW: String)
}