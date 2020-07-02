package org.goblinframework.dao.mysql.ql;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Auto generate parameter name.
 */
public class ParameterNameGenerator {

  private final String prefix;
  private final AtomicInteger counter = new AtomicInteger(0);

  public ParameterNameGenerator(String prefix) {
    this.prefix = prefix;
  }

  public String next() {
    return String.format("%s%d", prefix, counter.incrementAndGet());
  }
}
