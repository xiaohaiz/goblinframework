package org.goblinframework.cache.couchbase.provider;

import com.couchbase.client.java.document.LegacyDocument;
import com.couchbase.client.java.error.DocumentAlreadyExistsException;
import com.couchbase.client.java.error.DocumentDoesNotExistException;
import org.goblinframework.cache.core.*;
import org.goblinframework.cache.core.cache.AbstractCache;
import org.goblinframework.cache.couchbase.client.CouchbaseClient;
import org.goblinframework.cache.couchbase.module.exception.CouchbaseCacheException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rx.Observable;

import java.io.Serializable;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

final public class CouchbaseCache extends AbstractCache {

  private final CouchbaseClient client;

  public CouchbaseCache(@NotNull String name, @NotNull CouchbaseClient client) {
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
      throw new CouchbaseCacheException(ex);
    }
    if (document == null) {
      return new GetResult<>(key);
    }
    Object cached = document.content();
    GetResult<T> gr = new GetResult<>(key);
    gr.cas = document.cas();
    gr.hit = true;
    if (cached instanceof CacheValueWrapper) {
      gr.wrapper = true;
      gr.uncheckedSetValue(((CacheValueWrapper) cached).getValue());
    } else {
      gr.uncheckedSetValue(cached);
    }
    return gr;
  }

  @NotNull
  @Override
  public <T> Map<String, GetResult<T>> gets(@Nullable Collection<String> keys) {
    if (keys == null || keys.isEmpty()) {
      return Collections.emptyMap();
    }
    String[] ids = keys.stream().distinct().toArray(String[]::new);
    AtomicReference<Throwable> errorReference = new AtomicReference<>();
    Map<String, GetResult<T>> result = new LinkedHashMap<>();
    rx.Observable
        .from(ids)
        .flatMap(id -> client.getBucket().async().get(id, LegacyDocument.class)
            .doOnError(errorReference::set)
            .onErrorResumeNext(rx.Observable.empty()))
        .toBlocking()
        .forEach(d -> {
          String id = d.id();
          Object cached = d.content();
          GetResult<T> gr = new GetResult<>(id);
          gr.cas = d.cas();
          gr.hit = true;
          if (cached instanceof CacheValueWrapper) {
            gr.wrapper = true;
            gr.uncheckedSetValue(((CacheValueWrapper) cached).getValue());
          } else {
            gr.uncheckedSetValue(cached);
          }
          result.put(id, gr);
        });
    if (errorReference.get() != null) {
      throw new CouchbaseCacheException(errorReference.get());
    }
    for (String id : ids) {
      if (!result.containsKey(id)) {
        result.put(id, new GetResult<>(id));
      }
    }
    return result;
  }

  @Override
  public boolean delete(@Nullable String key) {
    if (key == null) {
      return false;
    }
    try {
      return client.getBucket().remove(key) != null;
    } catch (Exception ex) {
      throw new CouchbaseCacheException(ex);
    }
  }

  @Override
  public void deletes(@Nullable Collection<String> keys) {
    if (keys == null || keys.isEmpty()) return;
    String[] keyList = keys.stream().filter(Objects::nonNull).distinct().toArray(String[]::new);
    if (keyList.length == 0) return;

    AtomicReference<Throwable> errorReference = new AtomicReference<>();
    rx.Observable.from(keyList)
        .flatMap(id ->
            client.getBucket().async().remove(id)
                .doOnError(error -> {
                  if (!(error instanceof DocumentDoesNotExistException)) {
                    errorReference.set(error);
                  }
                })
                .onErrorResumeNext(Observable.empty()))
        .toBlocking()
        .lastOrDefault(null);
    if (errorReference.get() != null) {
      throw new CouchbaseCacheException(errorReference.get());
    }
  }

  @Override
  public <T> boolean add(@Nullable String key, int expirationInSeconds, @Nullable T value) {
    if (key == null || expirationInSeconds < 0 || value == null) {
      return false;
    }
    int expiry = calculateExpiration(expirationInSeconds);
    if (!(value instanceof Serializable)) {
      throw new CouchbaseCacheException();
    }
    LegacyDocument document = LegacyDocument.create(key, expiry, value);
    try {
      client.getBucket().insert(document);
      return true;
    } catch (DocumentAlreadyExistsException ex) {
      return false;
    } catch (Exception ex) {
      throw new CouchbaseCacheException(ex);
    }
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
    client.flush();
  }

  private int calculateExpiration(int seconds) {
    int epochSecond = (int) Instant.now().getEpochSecond();
    if (seconds > epochSecond) {
      // yes, it's already unix time.
      return seconds;
    }
    if (seconds >= 864000000) {
      // TTL more than 10K days? not acceptable
      // treat it as unix time
      return seconds;
    }
    if (seconds >= 86400 * 30) {
      seconds = epochSecond + seconds;
    }
    return seconds;
  }
}
