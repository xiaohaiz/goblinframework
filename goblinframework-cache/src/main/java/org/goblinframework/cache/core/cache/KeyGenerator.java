package org.goblinframework.cache.core.cache;

public interface KeyGenerator<K> {
  String generate(K source);
}
