package org.goblinframework.core.util

import org.junit.Assert.assertEquals
import org.junit.Test

class TranscoderUtilsTest {

  @Test
  fun packZeros() {
    for (i in 0..9999) {
      val si = RandomUtils.nextInt()
      var bs = TranscoderUtils.encodeIntPackZeros(si)
      val ti = TranscoderUtils.decodeInt(bs)
      assertEquals(si, ti)

      val sl = RandomUtils.nextLong()
      bs = TranscoderUtils.encodeLongPackZeros(sl)
      val tl = TranscoderUtils.decodeLong(bs)
      assertEquals(sl, tl)
    }
  }

  @Test
  fun noPackZeros() {
    for (i in 0..9999) {
      val si = RandomUtils.nextInt()
      var bs = TranscoderUtils.encodeIntNoPackZeros(si)
      val ti = TranscoderUtils.decodeInt(bs)
      assertEquals(si, ti)

      val sl = RandomUtils.nextLong()
      bs = TranscoderUtils.encodeLongNoPackZeros(sl)
      val tl = TranscoderUtils.decodeLong(bs)
      assertEquals(sl, tl)
    }
  }
}