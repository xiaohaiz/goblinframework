package org.goblinframework.cache.core.cache;

import java.util.Collection;
import java.util.Map;

public interface ExternalLoader<K, V> {
  Map<K, V> loadFromExternal(Collection<K> missedSources);
}
