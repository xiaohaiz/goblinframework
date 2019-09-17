package org.goblinframework.cache.redis.connection;

import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.goblinframework.cache.redis.transcoder.RedisTranscoder;
import org.jetbrains.annotations.NotNull;

public class ClusterRedisConnectionFactory extends BasePooledObjectFactory<RedisConnection> {

  private final RedisClusterClient redisClient;
  private final RedisTranscoder redisTranscoder;

  public ClusterRedisConnectionFactory(@NotNull RedisClusterClient redisClient,
                                       @NotNull RedisTranscoder redisTranscoder) {
    this.redisClient = redisClient;
    this.redisTranscoder = redisTranscoder;
  }

  @Override
  public RedisConnection create() {
    return createClusterRedisConnection(redisClient, redisTranscoder);
  }

  @Override
  public PooledObject<RedisConnection> wrap(RedisConnection obj) {
    return new DefaultPooledObject<>(obj);
  }

  @NotNull
  public static ClusterRedisConnection createClusterRedisConnection(@NotNull RedisClusterClient redisClient,
                                                                    @NotNull RedisTranscoder redisTranscoder) {
    StatefulRedisClusterConnection<String, Object> connection = redisClient.connect(redisTranscoder);
    return new ClusterRedisConnection(connection);
  }
}
