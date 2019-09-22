package org.goblinframework.core.cache;

public interface KeyGenerator<K> {
  String generate(K source);
}
