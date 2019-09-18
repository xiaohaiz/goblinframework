package org.goblinframework.cache.core.cache;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;

public interface GoblinCache extends CacheSystemLocationProvider {

  @NotNull
  Object getNativeCache();

  @Nullable
  <T> T load(@Nullable String key);

  @NotNull
  <T> GetResult<T> get(@Nullable String key);

  @Nullable
  <T> Map<String, GetResult<T>> gets(@Nullable Collection<String> keys);

  boolean delete(@Nullable String key);

  <T> boolean add(@Nullable String key, int expirationInSeconds, @Nullable T value);

  <T> boolean set(@Nullable String key, int expirationInSeconds, @Nullable T value);

  <T> boolean replace(@Nullable String key, int expirationInSeconds, @Nullable T value);

  <T> boolean append(@Nullable String key, @Nullable T value);

  boolean touch(@Nullable String key, int expirationInSeconds);

  long ttl(@Nullable String key);

  @Nullable
  Long incr(@Nullable String key, long delta, long initialValue, int expirationInSeconds);

  @Nullable
  Long decr(@Nullable String key, long delta, long initialValue, int expirationInSeconds);

  @Nullable
  <T> Boolean cas(@Nullable String key, int expirationInSeconds, @Nullable GetResult<T> getResult, @Nullable CasOperation<T> casOperation);

  @Nullable
  <T> Boolean cas(@Nullable String key, int expirationInSeconds, @Nullable GetResult<T> getResult, int maxTries, @Nullable CasOperation<T> casOperation);

}
