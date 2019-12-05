package org.goblinframework.cache.redis.connection;

import io.lettuce.core.api.StatefulRedisConnection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TransactionCallback<E> {

  @Nullable
  E execute(@NotNull String key, @NotNull StatefulRedisConnection<String, Object> connection);
}
