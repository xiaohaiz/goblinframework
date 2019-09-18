package org.goblinframework.cache.redis.client;

import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.goblinframework.cache.redis.command.RedisCommands;
import org.goblinframework.cache.redis.command.SingleRedisCommands;
import org.goblinframework.cache.redis.connection.RedisConnection;
import org.goblinframework.cache.redis.connection.SingleRedisConnection;
import org.goblinframework.cache.redis.connection.SingleRedisConnectionFactory;
import org.goblinframework.cache.redis.connection.TransactionCallback;
import org.goblinframework.cache.redis.module.config.RedisConfig;
import org.goblinframework.core.util.StringUtils;
import org.jetbrains.annotations.NotNull;

final public class SingleRedisClient extends RedisClient {

  private final io.lettuce.core.RedisClient client;
  private final SingleRedisConnection connection;
  private final ObjectPool<RedisConnection> connectionPool;
  private final RedisCommands commands;

  public SingleRedisClient(@NotNull RedisConfig config) {
    super(config);
    String[] s = StringUtils.split(config.getServers(), " ");
    if (s.length != 1) {
      throw new IllegalArgumentException("Malformed single REDIS servers configuration, one host:port is required.");
    }
    String host = StringUtils.substringBefore(s[0], ":");
    int port = NumberUtils.toInt(StringUtils.substringAfter(s[0], ":"));

    RedisURI.Builder builder = RedisURI.builder().withHost(host).withPort(port);
    if (config.getMapper().getPassword() != null) {
      builder.withPassword(config.getMapper().getPassword());
    }
    RedisURI uri = builder.build();
    this.client = io.lettuce.core.RedisClient.create(uri);
    this.connection = SingleRedisConnectionFactory.createSingleRedisConnection(client, getTranscoder());
    this.connectionPool = new GenericObjectPool<>(new SingleRedisConnectionFactory(client, getTranscoder()), getPoolConfig());
    this.commands = new SingleRedisCommands(connection.getNativeConnection());
  }

  @NotNull
  @Override
  public SingleRedisConnection getConnection() {
    return connection;
  }

  @NotNull
  @Override
  public SingleRedisConnection openPooledConnection() {
    try {
      SingleRedisConnection connection = (SingleRedisConnection) connectionPool.borrowObject();
      connection.setConnectionPool(connectionPool);
      return connection;
    } catch (Exception ex) {
      throw new RedisClientException("No more available REDIS connection", ex);
    }
  }

  @Override
  public void returnPooledConnection(@NotNull RedisConnection connection) {
    try {
      connectionPool.returnObject(connection);
    } catch (Exception ignore) {
    }
  }

  @Override
  public <E> E executeTransaction(@NotNull String key, @NotNull TransactionCallback<E> callback) {
    try (SingleRedisConnection connection = openPooledConnection()) {
      StatefulRedisConnection<String, Object> c = connection.getNativeConnection();
      return callback.execute(key, c);
    }
  }

  @NotNull
  @Override
  public RedisCommands getRedisCommands() {
    return commands;
  }

  @Override
  public void doFlush() {
    connection.getNativeConnection().sync().flushall();
  }

  @Override
  public void doDestroy() {
    connection.getNativeConnection().close();
    connectionPool.close();
    client.shutdown();
    logger.debug("REDIS client [{}] shutdown", getConfig().getName());
  }
}
