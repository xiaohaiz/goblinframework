package org.goblinframework.cache.redis.provider;

import io.lettuce.core.RedisFuture;
import io.lettuce.core.SetArgs;
import io.lettuce.core.api.async.RedisKeyAsyncCommands;
import io.lettuce.core.api.async.RedisStringAsyncCommands;
import org.goblinframework.cache.core.cache.*;
import org.goblinframework.cache.redis.client.RedisClient;
import org.goblinframework.core.exception.GoblinInterruptedException;
import org.goblinframework.core.mbean.GoblinManagedBean;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ExecutionException;

@GoblinManagedBean(type = "CACHE.REDIS")
final class RedisCacheImpl extends GoblinCacheImpl {

  private final RedisClient client;

  RedisCacheImpl(@NotNull String name, @NotNull RedisClient client) {
    super(new CacheSystemLocation(CacheSystem.RDS, name));
    this.client = client;
  }

  void destroy() {
    unregisterIfNecessary();
    logger.debug("REDIS cache [{}] closed", getName());
  }

  @SuppressWarnings("unchecked")
  @Nullable
  @Override
  public <T> GetResult<T> get(@Nullable String key) {
    if (key == null) {
      return new GetResult<>();
    }
    RedisStringAsyncCommands<String, Object> commands = client.getRedisCommands()
        .async().getRedisStringAsyncCommands();
    RedisFuture<Object> future = commands.get(key);
    Object cached;
    try {
      cached = future.get();
    } catch (InterruptedException ex) {
      throw new GoblinInterruptedException(ex);
    } catch (ExecutionException ex) {
      logger.error("RDS.get({})", key, ex);
      return null;
    }
    if (cached == null) {
      return new GetResult<>();
    }
    GetResult<T> gr = new GetResult<>();
    gr.cas = 0;
    gr.hit = true;
    if (cached instanceof CacheValueWrapper) {
      gr.wrapper = true;
      gr.value = (T) ((CacheValueWrapper) cached).getValue();
    } else {
      gr.value = (T) cached;
    }
    return gr;
  }

  @Nullable
  @Override
  public Boolean delete(@Nullable String key) {
    if (key == null) {
      return false;
    }
    RedisKeyAsyncCommands<String, Object> commands = client.getRedisCommands()
        .async().getRedisKeyAsyncCommands();
    RedisFuture<Long> future = commands.del(key);
    Long count;
    try {
      count = future.get();
    } catch (InterruptedException ex) {
      throw new GoblinInterruptedException(ex);
    } catch (ExecutionException ex) {
      logger.error("RDS.delete({})", key, ex);
      return null;
    }
    return count != null && count > 0;
  }

  @Nullable
  @Override
  public <T> Boolean add(@Nullable String key, int expirationInSeconds, @Nullable T value) {
    if (key == null || expirationInSeconds < 0 || value == null) {
      return false;
    }
    RedisStringAsyncCommands<String, Object> commands = client.getRedisCommands()
        .async().getRedisStringAsyncCommands();
    Boolean ret;
    if (expirationInSeconds > 0) {
      SetArgs args = new SetArgs().ex(expirationInSeconds).nx();
      String response;
      try {
        response = commands.set(key, value, args).get();
      } catch (InterruptedException ex) {
        throw new GoblinInterruptedException(ex);
      } catch (ExecutionException ex) {
        logger.error("RDS.add({})", key, ex);
        return null;
      }
      ret = "OK".equalsIgnoreCase(response);
    } else {
      try {
        ret = commands.setnx(key, value).get();
      } catch (InterruptedException ex) {
        throw new GoblinInterruptedException(ex);
      } catch (ExecutionException ex) {
        logger.error("RDS.add({})", key, ex);
        return null;
      }
    }
    return ret;
  }
}
