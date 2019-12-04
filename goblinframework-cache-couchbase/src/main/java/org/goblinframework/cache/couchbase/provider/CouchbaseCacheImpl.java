package org.goblinframework.cache.couchbase.provider;

import com.couchbase.client.java.document.LegacyDocument;
import org.goblinframework.cache.core.*;
import org.goblinframework.cache.core.cache.AbstractCache;
import org.goblinframework.cache.couchbase.client.CouchbaseClient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final public class CouchbaseCacheImpl extends AbstractCache {

  private final CouchbaseClient client;

  public CouchbaseCacheImpl(@NotNull String name, @NotNull CouchbaseClient client) {
    super(new CacheLocation(CacheSystem.CBS, name));
    this.client = client;
  }

  @NotNull
  @Override
  public CouchbaseClient nativeCache() {
    return client;
  }

  @NotNull
  @Override
  public <T> GetResult<T> get(@Nullable String key) {
    if (key == null) {
      return new GetResult<>(null);
    }
    LegacyDocument document = null;
    try {
      document = client.getBucket().get(key, LegacyDocument.class);
    } catch (Exception ex) {
      return new GetResult<>(null);
    }
    if (document == null) {
      return new GetResult<>(null);
    }
    long cas = document.cas();
    Object cached = document.content();
    GetResult<T> gr = new GetResult<>(key);
    gr.cas = 0;
    gr.hit = true;
    if (cached instanceof CacheValueWrapper) {
      gr.wrapper = true;
      gr.uncheckedSetValue(((CacheValueWrapper) cached).getValue());
    } else {
      gr.uncheckedSetValue(cached);
    }
    return gr;
  }

  @Override
  public boolean delete(@Nullable String key) {
    return false;
  }

  @Override
  public <T> boolean add(@Nullable String key, int expirationInSeconds, @Nullable T value) {
    return false;
  }

  @Override
  public <T> boolean set(@Nullable String key, int expirationInSeconds, @Nullable T value) {
    return false;
  }

  @Override
  public <T> boolean replace(@Nullable String key, int expirationInSeconds, @Nullable T value) {
    return false;
  }

  @Override
  public <T> boolean append(@Nullable String key, @Nullable T value) {
    return false;
  }

  @Override
  public boolean touch(@Nullable String key, int expirationInSeconds) {
    return false;
  }

  @Override
  public long ttl(@Nullable String key) {
    return 0;
  }

  @Override
  public long incr(@Nullable String key, long delta, long initialValue, int expirationInSeconds) {
    return 0;
  }

  @Override
  public long decr(@Nullable String key, long delta, long initialValue, int expirationInSeconds) {
    return 0;
  }

  @Override
  public <T> boolean cas(@Nullable String key, int expirationInSeconds, @Nullable GetResult<T> getResult, int maxTries, @Nullable CasOperation<T> casOperation) {
    return false;
  }

  @Override
  public void flush() {

  }
}
