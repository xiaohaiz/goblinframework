package org.goblinframework.cache.core.cache;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface GoblinCache extends CacheSystemLocationProvider {

  @NotNull
  Object getNativeCache();

  @Nullable
  <T> GetResult<T> get(@Nullable String key);

  @Nullable
  Boolean delete(@Nullable String key);

  @Nullable
  <T> Boolean add(@Nullable String key, int expirationInSeconds, @Nullable T value);

  @Nullable
  <T> Boolean set(@Nullable String key, int expirationInSeconds, @Nullable T value);

  @Nullable
  <T> Boolean replace(@Nullable String key, int expirationInSeconds, @Nullable T value);

  @Nullable
  <T> Boolean append(@Nullable String key, @Nullable T value);

  @Nullable
  Boolean touch(@Nullable String key, int expirationInSeconds);

  @Nullable
  Long ttl(@Nullable String key);

  @Nullable
  Long incr(@Nullable String key, long delta, long initialValue, int expirationInSeconds);

  @Nullable
  Long decr(@Nullable String key, long delta, long initialValue, int expirationInSeconds);

  @Nullable
  <T> Boolean cas(@Nullable String key, int expirationInSeconds, @Nullable GetResult<T> getResult, @Nullable CasOperation<T> casOperation);

  @Nullable
  <T> Boolean cas(@Nullable String key, int expirationInSeconds, @Nullable GetResult<T> getResult, int maxTries, @Nullable CasOperation<T> casOperation);

}
