package org.goblinframework.core.compress;

import kotlin.text.Charsets;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class CompressionUtilsTest {

  @Test
  public void compress() throws Exception {
    String s = RandomStringUtils.randomAlphanumeric(1024);
    ByteArrayInputStream bis = new ByteArrayInputStream(s.getBytes(Charsets.UTF_8));
    ByteArrayOutputStream bos = new ByteArrayOutputStream();

    CompressionUtils.compress(bis, bos);

    bis.close();
    bos.close();
    ;
  }
}