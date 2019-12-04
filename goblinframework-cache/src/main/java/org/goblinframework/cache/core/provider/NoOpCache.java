package org.goblinframework.cache.core.provider;

import org.goblinframework.api.annotation.Singleton;
import org.goblinframework.cache.core.*;
import org.goblinframework.cache.core.cache.CacheMXBean;
import org.goblinframework.cache.core.cache.CacheValueLoaderImpl;
import org.goblinframework.cache.core.cache.CacheValueModifierImpl;
import org.goblinframework.core.service.GoblinManagedObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Singleton
final public class NoOpCache extends GoblinManagedObject implements Cache, CacheMXBean {

  static final NoOpCache INSTANCE = new NoOpCache();

  private NoOpCache() {
  }

  @NotNull
  @Override
  public CacheLocation getCacheSystemLocation() {
    return new CacheLocation(CacheSystem.NOP, getCacheName());
  }

  @NotNull
  @Override
  public CacheSystem getCacheSystem() {
    return CacheSystem.NOP;
  }

  @NotNull
  @Override
  public String getCacheName() {
    return "NOP";
  }

  @NotNull
  @Override
  public Object nativeCache() {
    throw new UnsupportedOperationException();
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
    return null;
  }

  @NotNull
  @Override
  public <T> GetResult<T> get(@Nullable String key) {
    return new GetResult<>(key);
  }

  @NotNull
  @Override
  public <T> Map<String, GetResult<T>> gets(@Nullable Collection<String> keys) {
    return Collections.emptyMap();
  }

  @Override
  public boolean delete(@Nullable String key) {
    return true;
  }

  @Override
  public void deletes(@Nullable Collection<String> keys) {
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
  public <T> boolean cas(@Nullable String key, int expirationInSeconds, @Nullable GetResult<T> getResult, @Nullable CasOperation<T> casOperation) {
    return true;
  }

  @Override
  public <T> boolean cas(@Nullable String key, int expirationInSeconds, @Nullable GetResult<T> getResult, int maxTries, @Nullable CasOperation<T> casOperation) {
    return true;
  }

  @Override
  public void flush() {
  }
}
