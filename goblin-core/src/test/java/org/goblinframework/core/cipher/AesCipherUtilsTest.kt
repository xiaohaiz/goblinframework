package org.goblinframework.core.cipher

import org.apache.commons.lang3.RandomStringUtils
import org.goblinframework.test.runner.GoblinTestRunner
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration

@RunWith(GoblinTestRunner::class)
@ContextConfiguration("/UT.xml")
class AesCipherUtilsTest {
  @Test
  fun test() {
    val key = RandomStringUtils.randomAlphanumeric(16)
    val value = "test_value"
    val encryptStr = AesCipherUtils.encryptBase64String(key, value)
    println("BASE64: $encryptStr")
    val decryptStr = AesCipherUtils.decryptBase64String(key, encryptStr)
    println(decryptStr)
    Assert.assertEquals(value, decryptStr)
  }
}