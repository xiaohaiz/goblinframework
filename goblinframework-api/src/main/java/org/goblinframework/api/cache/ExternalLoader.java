package org.goblinframework.api.cache;

import java.util.Collection;
import java.util.Map;

public interface ExternalLoader<K, V> {
  Map<K, V> loadFromExternal(Collection<K> missedSources);
}
