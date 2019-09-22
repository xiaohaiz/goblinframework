package org.goblinframework.cache.core.provider;

import org.goblinframework.api.annotation.Singleton;
import org.goblinframework.cache.core.annotation.CacheSystem;
import org.goblinframework.cache.core.cache.AbstractGoblinCache;
import org.goblinframework.cache.core.cache.CacheSystemLocation;
import org.goblinframework.cache.core.cache.CasOperation;
import org.goblinframework.cache.core.cache.GetResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Singleton
final public class NoOpCache extends AbstractGoblinCache {

  static final NoOpCache INSTANCE = new NoOpCache();

  private NoOpCache() {
    super(new CacheSystemLocation(CacheSystem.NOP, "NOP"));
  }

  @NotNull
  @Override
  public Object getNativeCache() {
    throw new UnsupportedOperationException();
  }

  @NotNull
  @Override
  public <T> GetResult<T> get(@Nullable String key) {
    return new GetResult<>(key);
  }

  @Override
  public boolean delete(@Nullable String key) {
    return true;
  }

  @Override
  public <T> boolean add(@Nullable String key, int expirationInSeconds, @Nullable T value) {
    return true;
  }

  @Override
  public <T> boolean set(@Nullable String key, int expirationInSeconds, @Nullable T value) {
    return true;
  }

  @Override
  public <T> boolean replace(@Nullable String key, int expirationInSeconds, @Nullable T value) {
    return true;
  }

  @Override
  public <T> boolean append(@Nullable String key, @Nullable T value) {
    return true;
  }

  @Override
  public boolean touch(@Nullable String key, int expirationInSeconds) {
    return true;
  }

  @Override
  public long ttl(@Nullable String key) {
    throw new UnsupportedOperationException();
  }

  @Override
  public long incr(@Nullable String key, long delta, long initialValue, int expirationInSeconds) {
    throw new UnsupportedOperationException();
  }

  @Override
  public long decr(@Nullable String key, long delta, long initialValue, int expirationInSeconds) {
    throw new UnsupportedOperationException();
  }

  @Override
  public <T> boolean cas(@Nullable String key, int expirationInSeconds, @Nullable GetResult<T> getResult, int maxTries, @Nullable CasOperation<T> casOperation) {
    return true;
  }

  @Override
  public void flush() {
  }
}
