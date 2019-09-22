package org.goblinframework.cache.redis.provider;

import io.lettuce.core.KeyValue;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.SetArgs;
import io.lettuce.core.TransactionResult;
import io.lettuce.core.api.async.RedisKeyAsyncCommands;
import io.lettuce.core.api.async.RedisStringAsyncCommands;
import org.goblinframework.cache.core.cache.AbstractGoblinCache;
import org.goblinframework.cache.core.cache.CacheValueModifier;
import org.goblinframework.cache.core.cache.CacheValueWrapper;
import org.goblinframework.cache.redis.client.RedisClient;
import org.goblinframework.core.cache.CasOperation;
import org.goblinframework.core.cache.GetResult;
import org.goblinframework.core.cache.GoblinCacheSystem;
import org.goblinframework.core.cache.GoblinCacheSystemLocation;
import org.goblinframework.core.exception.GoblinExecutionException;
import org.goblinframework.core.exception.GoblinInterruptedException;
import org.goblinframework.core.util.NumberUtils;
import org.goblinframework.core.util.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ExecutionException;

final class RedisCacheImpl extends AbstractGoblinCache {

  private final RedisClient client;

  RedisCacheImpl(@NotNull String name, @NotNull RedisClient client) {
    super(new GoblinCacheSystemLocation(GoblinCacheSystem.RDS, name));
    this.client = client;
  }

  @NotNull
  @Override
  public RedisClient getNativeCache() {
    return client;
  }

  @NotNull
  @Override
  public <V> CacheValueModifier<V> modifier() {
    return new RedisCacheValueModifier<>(this);
  }

  @NotNull
  @Override
  public <T> GetResult<T> get(@Nullable String key) {
    if (key == null) {
      return new GetResult<>(null);
    }
    RedisStringAsyncCommands<String, Object> commands = client.getRedisCommands().async().getRedisStringAsyncCommands();
    RedisFuture<Object> future = commands.get(key);
    Object cached;
    try {
      cached = future.get();
    } catch (InterruptedException ex) {
      throw new GoblinInterruptedException(ex);
    } catch (ExecutionException ex) {
      logger.error("RDS.get({})", key, ex);
      throw new GoblinExecutionException(ex);
    }
    if (cached == null) {
      return new GetResult<>(key);
    }
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

  @NotNull
  @Override
  public <T> Map<String, GetResult<T>> gets(@Nullable Collection<String> keys) {
    if (keys == null || keys.isEmpty()) {
      return Collections.emptyMap();
    }
    String[] ids = keys.stream().distinct().toArray(String[]::new);
    RedisStringAsyncCommands<String, Object> commands = client.getRedisCommands().async().getRedisStringAsyncCommands();
    RedisFuture<List<KeyValue<String, Object>>> future = commands.mget(ids);
    List<KeyValue<String, Object>> kvs;
    try {
      kvs = future.get();
    } catch (InterruptedException ex) {
      throw new GoblinInterruptedException(ex);
    } catch (ExecutionException ex) {
      logger.error("RDS.gets({})", StringUtils.join(ids, " "), ex);
      throw new GoblinExecutionException(ex);
    }
    Map<String, GetResult<T>> result = new LinkedHashMap<>();
    for (KeyValue<String, Object> kv : kvs) {
      String id = kv.getKey();
      if (!kv.hasValue()) {
        result.put(id, new GetResult<>(id));
      } else {
        Object cached = kv.getValue();
        GetResult<T> gr = new GetResult<>(id);
        gr.cas = 0;
        gr.hit = true;
        if (cached instanceof CacheValueWrapper) {
          gr.wrapper = true;
          gr.uncheckedSetValue(((CacheValueWrapper) cached).getValue());
        } else {
          gr.uncheckedSetValue(cached);
        }
        result.put(id, gr);
      }
    }
    return result;
  }

  @Override
  public boolean delete(@Nullable String key) {
    if (key == null) {
      return false;
    }
    RedisKeyAsyncCommands<String, Object> commands = client.getRedisCommands().async().getRedisKeyAsyncCommands();
    RedisFuture<Long> future = commands.del(key);
    Long count;
    try {
      count = future.get();
    } catch (InterruptedException ex) {
      throw new GoblinInterruptedException(ex);
    } catch (ExecutionException ex) {
      logger.error("RDS.delete({})", key, ex);
      throw new GoblinExecutionException(ex);
    }
    return count != null && count > 0;
  }

  @Override
  public void deletes(@Nullable Collection<String> keys) {
    if (keys == null || keys.isEmpty()) return;
    String[] keyList = keys.stream().filter(Objects::nonNull).distinct().toArray(String[]::new);
    if (keyList.length == 0) return;
    RedisKeyAsyncCommands<String, Object> commands = client.getRedisCommands().async().getRedisKeyAsyncCommands();
    try {
      commands.del(keyList).get();
    } catch (InterruptedException ex) {
      throw new GoblinInterruptedException(ex);
    } catch (ExecutionException ex) {
      logger.error("RDS.deletes({})", StringUtils.join(keyList, " "), ex);
      throw new GoblinExecutionException(ex);
    }
  }

  @Override
  public <T> boolean add(@Nullable String key, int expirationInSeconds, @Nullable T value) {
    if (key == null || expirationInSeconds < 0 || value == null) {
      return false;
    }
    RedisStringAsyncCommands<String, Object> commands = client.getRedisCommands().async().getRedisStringAsyncCommands();
    boolean ret;
    if (expirationInSeconds > 0) {
      SetArgs args = new SetArgs().ex(expirationInSeconds).nx();
      String response;
      try {
        response = commands.set(key, value, args).get();
      } catch (InterruptedException ex) {
        throw new GoblinInterruptedException(ex);
      } catch (ExecutionException ex) {
        logger.error("RDS.add({})", key, ex);
        throw new GoblinExecutionException(ex);
      }
      ret = "OK".equalsIgnoreCase(response);
    } else {
      try {
        ret = commands.setnx(key, value).get();
      } catch (InterruptedException ex) {
        throw new GoblinInterruptedException(ex);
      } catch (ExecutionException ex) {
        logger.error("RDS.add({})", key, ex);
        throw new GoblinExecutionException(ex);
      }
    }
    return ret;
  }

  @Override
  public <T> boolean set(@Nullable String key, int expirationInSeconds, @Nullable T value) {
    if (key == null || expirationInSeconds < 0 || value == null) {
      return false;
    }
    RedisStringAsyncCommands<String, Object> commands = client.getRedisCommands().async().getRedisStringAsyncCommands();
    String ret;
    if (expirationInSeconds > 0) {
      try {
        ret = commands.setex(key, expirationInSeconds, value).get();
      } catch (InterruptedException ex) {
        throw new GoblinInterruptedException(ex);
      } catch (ExecutionException ex) {
        logger.error("RDS.set({})", key, ex);
        throw new GoblinExecutionException(ex);
      }
    } else {
      try {
        ret = commands.set(key, value).get();
      } catch (InterruptedException ex) {
        throw new GoblinInterruptedException(ex);
      } catch (ExecutionException ex) {
        logger.error("RDS.set({})", key, ex);
        throw new GoblinExecutionException(ex);
      }
    }
    return "OK".equalsIgnoreCase(ret);
  }

  @Override
  public <T> boolean replace(@Nullable String key, int expirationInSeconds, @Nullable T value) {
    if (key == null || value == null || expirationInSeconds < 0) {
      return false;
    }
    RedisStringAsyncCommands<String, Object> commands = client.getRedisCommands().async().getRedisStringAsyncCommands();
    String ret;
    if (expirationInSeconds > 0) {
      SetArgs args = new SetArgs().ex(expirationInSeconds).xx();
      try {
        ret = commands.set(key, value, args).get();
      } catch (InterruptedException ex) {
        throw new GoblinInterruptedException(ex);
      } catch (ExecutionException ex) {
        logger.error("RDS.replace({})", key, ex);
        throw new GoblinExecutionException(ex);
      }
    } else {
      SetArgs args = new SetArgs().xx();
      try {
        ret = commands.set(key, value, args).get();
      } catch (InterruptedException ex) {
        throw new GoblinInterruptedException(ex);
      } catch (ExecutionException ex) {
        logger.error("RDS.replace({})", key, ex);
        throw new GoblinExecutionException(ex);
      }
    }
    return "OK".equalsIgnoreCase(ret);
  }

  @Override
  public <T> boolean append(@Nullable String key, @Nullable T value) {
    if (key == null || !(value instanceof CharSequence)) {
      return false;
    }
    String s = ((CharSequence) value).toString();
    if (s.isEmpty()) {
      return false;
    }
    RedisStringAsyncCommands<String, Object> commands = client.getRedisCommands().async().getRedisStringAsyncCommands();
    Long ret;
    try {
      ret = commands.append(key, s).get();
    } catch (InterruptedException ex) {
      throw new GoblinInterruptedException(ex);
    } catch (ExecutionException ex) {
      logger.error("RDS.append({})", key, ex);
      throw new GoblinExecutionException(ex);
    }
    return NumberUtils.toLong(ret) > s.length();
  }

  @Override
  public boolean touch(@Nullable String key, int expirationInSeconds) {
    if (key == null || expirationInSeconds < 0) {
      return false;
    }
    RedisKeyAsyncCommands<String, Object> commands = client.getRedisCommands().async().getRedisKeyAsyncCommands();
    Boolean ret;
    if (expirationInSeconds == 0) {
      try {
        ret = commands.persist(key).get();
      } catch (InterruptedException ex) {
        throw new GoblinInterruptedException(ex);
      } catch (ExecutionException ex) {
        logger.error("RDS.touch({})", key, ex);
        throw new GoblinExecutionException(ex);
      }
    } else {
      try {
        ret = commands.expire(key, expirationInSeconds).get();
      } catch (InterruptedException ex) {
        throw new GoblinInterruptedException(ex);
      } catch (ExecutionException ex) {
        logger.error("RDS.touch({})", key, ex);
        throw new GoblinExecutionException(ex);
      }
    }
    return ret != null && ret;
  }

  @Override
  public long ttl(@Nullable String key) {
    if (key == null) {
      throw new IllegalArgumentException();
    }
    RedisKeyAsyncCommands<String, Object> commands = client.getRedisCommands().async().getRedisKeyAsyncCommands();
    try {
      Long ret = commands.ttl(key).get();
      return NumberUtils.toLong(ret, -1);
    } catch (InterruptedException ex) {
      throw new GoblinInterruptedException(ex);
    } catch (ExecutionException ex) {
      logger.error("RDS.ttl({})", key, ex);
      throw new GoblinExecutionException(ex);
    }
  }

  @Override
  public long incr(@Nullable String key, long delta, long initialValue, int expirationInSeconds) {
    if (key == null || expirationInSeconds < 0) {
      throw new IllegalArgumentException();
    }
    if (delta < 0) {
      return decr(key, -delta, initialValue, expirationInSeconds);
    }
    long init = initialValue - delta;
    return client.executeTransaction(key, (id, connection) -> {
      connection.sync().multi();

      // execution 1
      if (expirationInSeconds > 0) {
        SetArgs args = new SetArgs().ex(expirationInSeconds).nx();
        connection.sync().set(id, Long.toString(init), args);
      } else {
        connection.sync().setnx(id, Long.toString(init));
      }

      // execution 2
      connection.sync().incrby(id, delta);

      TransactionResult tr = connection.sync().exec();
      if (tr.size() != 2) {
        throw new UnsupportedOperationException();
      }
      Object last = tr.get(1);
      return NumberUtils.toLong(last);
    });
  }

  @Override
  public long decr(@Nullable String key, long delta, long initialValue, int expirationInSeconds) {
    if (key == null || expirationInSeconds < 0) {
      throw new IllegalArgumentException();
    }
    if (delta < 0) {
      return incr(key, -delta, initialValue, expirationInSeconds);
    }
    long init = initialValue + delta;
    return client.executeTransaction(key, (id, connection) -> {
      connection.sync().multi();

      // execution 1
      if (expirationInSeconds > 0) {
        SetArgs args = new SetArgs().nx().ex(expirationInSeconds);
        connection.sync().set(id, Long.toString(init), args);
      } else {
        connection.sync().setnx(id, Long.toString(init));
      }

      // execution 2
      connection.sync().decrby(id, delta);

      TransactionResult tr = connection.sync().exec();
      if (tr.size() != 2) {
        throw new UnsupportedOperationException();
      }
      Object last = tr.get(1);
      return NumberUtils.toLong(last);
    });
  }

  @Override
  public <T> boolean cas(@Nullable String key, int expirationInSeconds,
                         @Nullable GetResult<T> getResult,  /* useless argument */
                         int maxTries, @Nullable CasOperation<T> casOperation) {
    if (key == null || expirationInSeconds < 0 || maxTries < 0 || casOperation == null) {
      return false;
    }
    int tries = 0;
    while (tries <= maxTries) {
      Boolean ret = client.executeTransaction(key, (id, connection) -> {
        connection.sync().watch(id);
        try {
          Object cached = connection.sync().get(id);
          if (cached == null) {
            return false;
          }
          Object current = cached;
          if (cached instanceof CacheValueWrapper) {
            current = ((CacheValueWrapper) cached).getValue();
          }
          @SuppressWarnings("unchecked")
          Object modified = casOperation.changeCacheObject((T) current);
          if (modified == null) {
            modified = new CacheValueWrapper(null);
          }

          connection.sync().multi();
          if (expirationInSeconds > 0) {
            connection.sync().setex(id, expirationInSeconds, modified);
          } else {
            connection.sync().set(id, modified);
          }
          TransactionResult tr = connection.sync().exec();
          for (Object o : tr) {
            if (o != null && "OK".equalsIgnoreCase(o.toString())) {
              return true;
            }
          }
          return false;
        } finally {
          connection.sync().unwatch();
        }
      });
      if (ret != null) {
        return ret;
      }
      tries++;
    }
    return false;
  }

  @Override
  public void flush() {
    client.flush();
  }
}
