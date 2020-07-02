package org.goblinframework.core.util;

import java.security.MessageDigest;

public class DigestUtils extends org.apache.commons.codec.digest.DigestUtils {

  public DigestUtils(MessageDigest digest) {
    super(digest);
  }

  public DigestUtils(String name) {
    super(name);
  }
}
