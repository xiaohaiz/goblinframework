package org.goblinframework.cache.redis.client;

import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.SlotHash;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.internal.HostAndPort;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.goblinframework.cache.redis.command.ClusterRedisCommands;
import org.goblinframework.cache.redis.command.RedisCommands;
import org.goblinframework.cache.redis.connection.ClusterRedisConnection;
import org.goblinframework.cache.redis.connection.ClusterRedisConnectionFactory;
import org.goblinframework.cache.redis.connection.RedisConnection;
import org.goblinframework.cache.redis.connection.TransactionCallback;
import org.goblinframework.cache.redis.module.config.RedisConfig;
import org.goblinframework.core.util.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ClusterRedisClient extends RedisClient {

  private final RedisClusterClient client;
  private final ClusterRedisConnection connection;
  private final ObjectPool<RedisConnection> connectionPool;
  private final RedisCommands commands;

  public ClusterRedisClient(@NotNull RedisConfig config) {
    super(config);

    List<RedisURI> uriList = Arrays.stream(StringUtils.split(config.getServers(), " "))
        .map(HostAndPort::parse)
        .map(e -> {
          RedisURI.Builder builder = RedisURI.builder()
              .withHost(e.getHostText())
              .withPort(e.getPort());
          if (config.getMapper().getPassword() != null) {
            builder = builder.withPassword(config.getMapper().getPassword());
          }
          return builder.build();
        })
        .collect(Collectors.toList());

    this.client = RedisClusterClient.create(uriList);
    this.connection = ClusterRedisConnectionFactory.createClusterRedisConnection(client, getTranscoder());
    this.connectionPool = new GenericObjectPool<>(new ClusterRedisConnectionFactory(client, getTranscoder()), getPoolConfig());
    this.commands = new ClusterRedisCommands(connection.getNativeConnection());
  }

  @Override
  public void doFlush() {
    connection.getNativeConnection().sync().flushall();
    logger.info("REDIS client [{}] flushed", getConfig().getName());
  }

  @Override
  public void doDispose() {
    connection.getNativeConnection().close();
    connectionPool.close();
    client.shutdown();
    logger.debug("REDIS client [{}] shutdown", getConfig().getName());
  }

  @NotNull
  @Override
  public ClusterRedisConnection getConnection() {
    return connection;
  }

  @NotNull
  @Override
  public ClusterRedisConnection openPooledConnection() {
    try {
      ClusterRedisConnection connection = (ClusterRedisConnection) connectionPool.borrowObject();
      connection.setConnectionPool(connectionPool);
      return connection;
    } catch (Exception ex) {
      throw new RedisClientException("No more available REDIS connection", ex);
    }
  }

  @Override
  public void returnPooledConnection(@NotNull RedisConnection connection) {
    try {
      this.connectionPool.returnObject(connection);
    } catch (Exception ignored) {
    }
  }

  @Override
  public <E> E executeTransaction(@NotNull String key, @NotNull TransactionCallback<E> callback) {
    try (ClusterRedisConnection connection = openPooledConnection()) {
      StatefulRedisClusterConnection<String, Object> nativeConnection = connection.getNativeConnection();
      int slot = SlotHash.getSlot(key);
      String nodeId = nativeConnection.getPartitions().getPartitionBySlot(slot).getNodeId();
      StatefulRedisConnection<String, Object> c = nativeConnection.getConnection(nodeId);
      return callback.execute(key, c);
    }
  }

  @NotNull
  @Override
  public RedisCommands getRedisCommands() {
    return commands;
  }
}
