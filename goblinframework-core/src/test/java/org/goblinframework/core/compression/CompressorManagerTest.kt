package org.goblinframework.core.compression

import org.apache.commons.lang3.RandomStringUtils
import org.goblinframework.test.runner.GoblinTestRunner
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.slf4j.LoggerFactory
import org.springframework.test.context.ContextConfiguration
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

@RunWith(GoblinTestRunner::class)
@ContextConfiguration("/UT.xml")
class CompressorManagerTest {

  companion object {
    private val logger = LoggerFactory.getLogger(CompressorManagerTest::class.java)
  }

  @Test
  fun compression() {
    val cm = CompressorManager.INSTANCE
    for (mode in CompressorMode.values()) {
      val c = cm.getCompressor(mode)

      val s = RandomStringUtils.randomAlphanumeric(1024 * 1024)
      var i = s.byteInputStream(Charsets.UTF_8)
      var o = ByteArrayOutputStream()
      c.compress(i, o)
      val compressed = o.toByteArray()
      i.close()
      o.close()

      logger.debug("Compressor[$mode] compressedSize=${compressed.size}")

      i = ByteArrayInputStream(compressed)
      o = ByteArrayOutputStream()
      c.decompress(i, o)
      val t = o.toByteArray().toString(Charsets.UTF_8)
      i.close()
      o.close()

      Assert.assertEquals(s, t)
    }
  }
}