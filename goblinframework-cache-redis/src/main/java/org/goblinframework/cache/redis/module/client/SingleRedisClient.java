package org.goblinframework.cache.redis.module.client;

import io.lettuce.core.RedisURI;
import org.apache.commons.lang3.math.NumberUtils;
import org.goblinframework.cache.redis.module.config.RedisConfig;
import org.goblinframework.core.util.StringUtils;
import org.jetbrains.annotations.NotNull;

final public class SingleRedisClient extends RedisClient {

  private final io.lettuce.core.RedisClient client;

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
  }

  @Override
  public void doDestroy() {
    client.shutdown();
    logger.debug("REDIS client [{}] shutdown", getConfig().getName());
  }
}
