package org.goblinframework.api.cache;

public interface KeyGenerator<K> {
  String generate(K source);
}
