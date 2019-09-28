package org.goblinframework.cache.core;

public interface KeyGenerator<K> {
  String generate(K source);
}
