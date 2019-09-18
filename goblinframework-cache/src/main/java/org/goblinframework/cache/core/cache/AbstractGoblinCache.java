package org.goblinframework.cache.core.cache;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

abstract public class AbstractGoblinCache implements GoblinCache {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  private final CacheSystemLocation location;

  protected AbstractGoblinCache(@NotNull CacheSystemLocation location) {
    this.location = location;
  }

  @NotNull
  @Override
  public CacheSystemLocation getCacheSystemLocation() {
    return location;
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
  public <T> boolean cas(@Nullable String key, int expirationInSeconds, @Nullable GetResult<T> getResult, @Nullable CasOperation<T> casOperation) {
    int maxTries = casOperation == null ? 0 : casOperation.getMaxTries();
    return cas(key, expirationInSeconds, getResult, maxTries, casOperation);
  }
}
