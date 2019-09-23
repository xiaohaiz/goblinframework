package org.goblinframework.cache.core.cache;

import org.goblinframework.api.cache.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

abstract public class AbstractGoblinCache implements GoblinCache {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  private final GoblinCacheSystemLocation location;

  protected AbstractGoblinCache(@NotNull GoblinCacheSystemLocation location) {
    this.location = location;
  }

  @NotNull
  @Override
  public GoblinCacheSystemLocation getCacheSystemLocation() {
    return location;
  }

  @NotNull
  @Override
  public <K, V> CacheValueLoader<K, V> loader() {
    return new CacheValueLoaderImpl<>(this);
  }

  @NotNull
  @Override
  public <V> CacheValueModifier<V> modifier() {
    return new CacheValueModifierImpl<>(this);
  }

  @Nullable
  @Override
  public <T> T load(@Nullable String key) {
    GetResult<T> gr = get(key);
    return gr.value;
  }

  @NotNull
  @Override
  public <T> Map<String, GetResult<T>> gets(@Nullable Collection<String> keys) {
    if (keys == null || keys.isEmpty()) {
      return Collections.emptyMap();
    }
    Map<String, GetResult<T>> result = new LinkedHashMap<>();
    keys.stream().distinct().forEach(id -> {
      GetResult<T> gr = get(id);
      result.put(id, gr);
    });
    return result;
  }

  @Override
  public void deletes(@Nullable Collection<String> keys) {
    if (keys == null || keys.isEmpty()) return;
    keys.stream().filter(Objects::nonNull).distinct().forEach(this::delete);
  }

  @Override
  public <T> boolean cas(@Nullable String key, int expirationInSeconds, @Nullable GetResult<T> getResult, @Nullable CasOperation<T> casOperation) {
    int maxTries = casOperation == null ? 0 : casOperation.getMaxTries();
    return cas(key, expirationInSeconds, getResult, maxTries, casOperation);
  }
}
