package org.goblinframework.api.function;

@FunctionalInterface
public interface Ordered {

  int HIGHEST_PRECEDENCE = Integer.MIN_VALUE;
  int LOWEST_PRECEDENCE = Integer.MAX_VALUE;

  int getOrder();

}
