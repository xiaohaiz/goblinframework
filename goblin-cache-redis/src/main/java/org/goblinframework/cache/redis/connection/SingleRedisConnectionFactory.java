package org.goblinframework.cache.redis.connection;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.goblinframework.cache.redis.interceptor.StatefulRedisConnectionInterceptor;
import org.goblinframework.cache.redis.transcoder.RedisTranscoder;
import org.goblinframework.core.util.ProxyUtils;
import org.jetbrains.annotations.NotNull;

public class SingleRedisConnectionFactory extends BasePooledObjectFactory<RedisConnection> {

  private final RedisClient redisClient;
  private final RedisTranscoder redisTranscoder;

  public SingleRedisConnectionFactory(@NotNull RedisClient redisClient,
                                      @NotNull RedisTranscoder redisTranscoder) {
    this.redisClient = redisClient;
    this.redisTranscoder = redisTranscoder;
  }

  @NotNull
  @Override
  public RedisConnection create() {
    return createSingleRedisConnection(redisClient, redisTranscoder);
  }

  @NotNull
  @Override
  public PooledObject<RedisConnection> wrap(@NotNull RedisConnection obj) {
    return new DefaultPooledObject<>(obj);
  }

  @Override
  public void destroyObject(PooledObject<RedisConnection> p) throws Exception {
    p.getObject().closeNativeConnection();
  }

  @SuppressWarnings("unchecked")
  @NotNull
  public static SingleRedisConnection createSingleRedisConnection(@NotNull RedisClient redisClient,
                                                                  @NotNull RedisTranscoder redisTranscoder) {
    StatefulRedisConnection<String, Object> connection = redisClient.connect(redisTranscoder);
    StatefulRedisConnection<String, Object> proxy = ProxyUtils.createInterfaceProxy(StatefulRedisConnection.class, new StatefulRedisConnectionInterceptor(connection));
    return new SingleRedisConnection(proxy);
  }
}
