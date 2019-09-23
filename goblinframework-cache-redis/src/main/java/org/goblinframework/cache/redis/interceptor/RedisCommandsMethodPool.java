package org.goblinframework.cache.redis.interceptor;

import io.lettuce.core.*;
import io.lettuce.core.api.async.*;
import io.lettuce.core.api.sync.*;
import io.lettuce.core.output.KeyStreamingChannel;
import io.lettuce.core.output.KeyValueStreamingChannel;
import io.lettuce.core.output.ScoredValueStreamingChannel;
import io.lettuce.core.output.ValueStreamingChannel;
import org.goblinframework.cache.redis.module.monitor.RDS;

import java.lang.reflect.Method;
import java.util.*;

final public class RedisCommandsMethodPool {

  private static final Map<Method, Integer> methodPool;

  static {
    methodPool = new HashMap<>();
    try {
      initialize();
    } catch (NoSuchMethodException ex) {
      throw new UnsupportedOperationException(ex);
    }
  }

  static void extractKeysIfNecessary(RDS instruction, Method method, Object[] arguments) {
    if (arguments == null || arguments.length == 0) {
      return;
    }
    try {
      int mode = methodPool.getOrDefault(method, -1);
      switch (mode) {
        case 1: {
          Object k = arguments[0];
          String s = (k == null ? "" : k.toString());
          instruction.keys = Collections.singletonList(s);
          break;
        }
        case 2: {
          Object k = arguments[1];
          String s = (k == null ? "" : k.toString());
          instruction.keys = Collections.singletonList(s);
          break;
        }
        case 11: {
          Object[] ks = (Object[]) arguments[0];
          List<String> sl = new ArrayList<>();
          if (ks != null) {
            for (Object k : ks) {
              sl.add(k == null ? "" : k.toString());
            }
          }
          instruction.keys = sl;
          break;
        }
        case 12: {
          Object[] ks = (Object[]) arguments[1];
          List<String> sl = new ArrayList<>();
          if (ks != null) {
            for (Object k : ks) {
              sl.add(k == null ? "" : k.toString());
            }
          }
          instruction.keys = sl;
          break;
        }
        case 21: {
          Map map = (Map) arguments[0];
          if (map != null && !map.isEmpty()) {
            List<String> sl = new ArrayList<>();
            for (Object k : map.keySet()) {
              sl.add(k == null ? "" : k.toString());
            }
            instruction.keys = sl;
          }
          break;
        }
        default: {
          break;
        }
      }
    } catch (Exception ignored) {
    }
  }


  private static void initialize() throws NoSuchMethodException {

    ///////////////////////////////////////////////////////////////////////////
    // RedisHashCommands
    ///////////////////////////////////////////////////////////////////////////

    methodPool.put(RedisHashCommands.class.getMethod("hdel", Object.class, Object[].class), 1);
    methodPool.put(RedisHashCommands.class.getMethod("hexists", Object.class, Object.class), 1);
    methodPool.put(RedisHashCommands.class.getMethod("hget", Object.class, Object.class), 1);
    methodPool.put(RedisHashCommands.class.getMethod("hincrby", Object.class, Object.class, long.class), 1);
    methodPool.put(RedisHashCommands.class.getMethod("hincrbyfloat", Object.class, Object.class, double.class), 1);
    methodPool.put(RedisHashCommands.class.getMethod("hgetall", Object.class), 1);
    methodPool.put(RedisHashCommands.class.getMethod("hgetall", KeyValueStreamingChannel.class, Object.class), 2);
    methodPool.put(RedisHashCommands.class.getMethod("hkeys", Object.class), 1);
    methodPool.put(RedisHashCommands.class.getMethod("hkeys", KeyStreamingChannel.class, Object.class), 2);
    methodPool.put(RedisHashCommands.class.getMethod("hlen", Object.class), 1);
    methodPool.put(RedisHashCommands.class.getMethod("hmget", Object.class, Object[].class), 1);
    methodPool.put(RedisHashCommands.class.getMethod("hmget", ValueStreamingChannel.class, Object.class, Object[].class), 2);
    methodPool.put(RedisHashCommands.class.getMethod("hmset", Object.class, Map.class), 1);
    methodPool.put(RedisHashCommands.class.getMethod("hscan", Object.class), 1);
    methodPool.put(RedisHashCommands.class.getMethod("hscan", Object.class, ScanArgs.class), 1);
    methodPool.put(RedisHashCommands.class.getMethod("hscan", Object.class, ScanCursor.class, ScanArgs.class), 1);
    methodPool.put(RedisHashCommands.class.getMethod("hscan", Object.class, ScanCursor.class), 1);
    methodPool.put(RedisHashCommands.class.getMethod("hscan", KeyValueStreamingChannel.class, Object.class), 2);
    methodPool.put(RedisHashCommands.class.getMethod("hscan", KeyValueStreamingChannel.class, Object.class, ScanArgs.class), 2);
    methodPool.put(RedisHashCommands.class.getMethod("hscan", KeyValueStreamingChannel.class, Object.class, ScanCursor.class, ScanArgs.class), 2);
    methodPool.put(RedisHashCommands.class.getMethod("hscan", KeyValueStreamingChannel.class, Object.class, ScanCursor.class), 2);
    methodPool.put(RedisHashCommands.class.getMethod("hset", Object.class, Object.class, Object.class), 1);
    methodPool.put(RedisHashCommands.class.getMethod("hsetnx", Object.class, Object.class, Object.class), 1);
    methodPool.put(RedisHashCommands.class.getMethod("hstrlen", Object.class, Object.class), 1);
    methodPool.put(RedisHashCommands.class.getMethod("hvals", Object.class), 1);
    methodPool.put(RedisHashCommands.class.getMethod("hvals", ValueStreamingChannel.class, Object.class), 2);

    ///////////////////////////////////////////////////////////////////////////
    // RedisKeyCommands
    ///////////////////////////////////////////////////////////////////////////

    methodPool.put(RedisKeyCommands.class.getMethod("del", Object[].class), 11);
    methodPool.put(RedisKeyCommands.class.getMethod("unlink", Object[].class), 11);
    methodPool.put(RedisKeyCommands.class.getMethod("dump", Object.class), 1);
    methodPool.put(RedisKeyCommands.class.getMethod("exists", Object[].class), 11);
    methodPool.put(RedisKeyCommands.class.getMethod("expire", Object.class, long.class), 1);
    methodPool.put(RedisKeyCommands.class.getMethod("expireat", Object.class, Date.class), 1);
    methodPool.put(RedisKeyCommands.class.getMethod("expireat", Object.class, long.class), 1);
    methodPool.put(RedisKeyCommands.class.getMethod("move", Object.class, int.class), 1);
    methodPool.put(RedisKeyCommands.class.getMethod("objectEncoding", Object.class), 1);
    methodPool.put(RedisKeyCommands.class.getMethod("objectIdletime", Object.class), 1);
    methodPool.put(RedisKeyCommands.class.getMethod("objectRefcount", Object.class), 1);
    methodPool.put(RedisKeyCommands.class.getMethod("persist", Object.class), 1);
    methodPool.put(RedisKeyCommands.class.getMethod("pexpire", Object.class, long.class), 1);
    methodPool.put(RedisKeyCommands.class.getMethod("pexpireat", Object.class, Date.class), 1);
    methodPool.put(RedisKeyCommands.class.getMethod("pexpireat", Object.class, long.class), 1);
    methodPool.put(RedisKeyCommands.class.getMethod("pttl", Object.class), 1);
    methodPool.put(RedisKeyCommands.class.getMethod("rename", Object.class, Object.class), 1);
    methodPool.put(RedisKeyCommands.class.getMethod("renamenx", Object.class, Object.class), 1);
    methodPool.put(RedisKeyCommands.class.getMethod("restore", Object.class, long.class, byte[].class), 1);
    methodPool.put(RedisKeyCommands.class.getMethod("sort", Object.class), 1);
    methodPool.put(RedisKeyCommands.class.getMethod("sort", ValueStreamingChannel.class, Object.class), 2);
    methodPool.put(RedisKeyCommands.class.getMethod("sort", Object.class, SortArgs.class), 1);
    methodPool.put(RedisKeyCommands.class.getMethod("sort", ValueStreamingChannel.class, Object.class, SortArgs.class), 2);
    methodPool.put(RedisKeyCommands.class.getMethod("sortStore", Object.class, SortArgs.class, Object.class), 1);
    methodPool.put(RedisKeyCommands.class.getMethod("touch", Object[].class), 11);
    methodPool.put(RedisKeyCommands.class.getMethod("ttl", Object.class), 1);
    methodPool.put(RedisKeyCommands.class.getMethod("type", Object.class), 1);

    ///////////////////////////////////////////////////////////////////////////
    // RedisStringCommands
    ///////////////////////////////////////////////////////////////////////////

    methodPool.put(RedisStringCommands.class.getMethod("append", Object.class, Object.class), 1);
    methodPool.put(RedisStringCommands.class.getMethod("bitcount", Object.class), 1);
    methodPool.put(RedisStringCommands.class.getMethod("bitcount", Object.class, long.class, long.class), 1);
    methodPool.put(RedisStringCommands.class.getMethod("bitfield", Object.class, BitFieldArgs.class), 1);
    methodPool.put(RedisStringCommands.class.getMethod("bitpos", Object.class, boolean.class), 1);
    methodPool.put(RedisStringCommands.class.getMethod("bitpos", Object.class, boolean.class, long.class, long.class), 1);
    methodPool.put(RedisStringCommands.class.getMethod("decr", Object.class), 1);
    methodPool.put(RedisStringCommands.class.getMethod("decrby", Object.class, long.class), 1);
    methodPool.put(RedisStringCommands.class.getMethod("get", Object.class), 1);
    methodPool.put(RedisStringCommands.class.getMethod("getbit", Object.class, long.class), 1);
    methodPool.put(RedisStringCommands.class.getMethod("getrange", Object.class, long.class, long.class), 1);
    methodPool.put(RedisStringCommands.class.getMethod("getset", Object.class, Object.class), 1);
    methodPool.put(RedisStringCommands.class.getMethod("incr", Object.class), 1);
    methodPool.put(RedisStringCommands.class.getMethod("incrby", Object.class, long.class), 1);
    methodPool.put(RedisStringCommands.class.getMethod("incrbyfloat", Object.class, double.class), 1);
    methodPool.put(RedisStringCommands.class.getMethod("mget", Object[].class), 11);
    methodPool.put(RedisStringCommands.class.getMethod("mget", ValueStreamingChannel.class, Object[].class), 12);
    methodPool.put(RedisStringCommands.class.getMethod("mset", Map.class), 21);
    methodPool.put(RedisStringCommands.class.getMethod("msetnx", Map.class), 21);
    methodPool.put(RedisStringCommands.class.getMethod("set", Object.class, Object.class), 1);
    methodPool.put(RedisStringCommands.class.getMethod("set", Object.class, Object.class, SetArgs.class), 1);
    methodPool.put(RedisStringCommands.class.getMethod("setbit", Object.class, long.class, int.class), 1);
    methodPool.put(RedisStringCommands.class.getMethod("setex", Object.class, long.class, Object.class), 1);
    methodPool.put(RedisStringCommands.class.getMethod("psetex", Object.class, long.class, Object.class), 1);
    methodPool.put(RedisStringCommands.class.getMethod("setnx", Object.class, Object.class), 1);
    methodPool.put(RedisStringCommands.class.getMethod("setrange", Object.class, long.class, Object.class), 1);
    methodPool.put(RedisStringCommands.class.getMethod("strlen", Object.class), 1);

    ///////////////////////////////////////////////////////////////////////////
    // RedisListCommands
    ///////////////////////////////////////////////////////////////////////////

    methodPool.put(RedisListCommands.class.getMethod("blpop", long.class, Object[].class), 12);
    methodPool.put(RedisListCommands.class.getMethod("brpop", long.class, Object[].class), 12);
    methodPool.put(RedisListCommands.class.getMethod("lindex", Object.class, long.class), 1);
    methodPool.put(RedisListCommands.class.getMethod("linsert", Object.class, boolean.class, Object.class, Object.class), 1);
    methodPool.put(RedisListCommands.class.getMethod("llen", Object.class), 1);
    methodPool.put(RedisListCommands.class.getMethod("lpop", Object.class), 1);
    methodPool.put(RedisListCommands.class.getMethod("lpush", Object.class, Object[].class), 1);
    methodPool.put(RedisListCommands.class.getMethod("lpushx", Object.class, Object.class), 1);
    methodPool.put(RedisListCommands.class.getMethod("lpushx", Object.class, Object[].class), 1);
    methodPool.put(RedisListCommands.class.getMethod("lrange", Object.class, long.class, long.class), 1);
    methodPool.put(RedisListCommands.class.getMethod("lrange", ValueStreamingChannel.class, Object.class, long.class, long.class), 2);
    methodPool.put(RedisListCommands.class.getMethod("lrem", Object.class, long.class, Object.class), 1);
    methodPool.put(RedisListCommands.class.getMethod("lset", Object.class, long.class, Object.class), 1);
    methodPool.put(RedisListCommands.class.getMethod("ltrim", Object.class, long.class, long.class), 1);
    methodPool.put(RedisListCommands.class.getMethod("rpop", Object.class), 1);
    methodPool.put(RedisListCommands.class.getMethod("rpush", Object.class, Object[].class), 1);
    methodPool.put(RedisListCommands.class.getMethod("rpushx", Object.class, Object.class), 1);
    methodPool.put(RedisListCommands.class.getMethod("rpushx", Object.class, Object[].class), 1);

    ///////////////////////////////////////////////////////////////////////////
    // RedisSetCommands
    ///////////////////////////////////////////////////////////////////////////

    methodPool.put(RedisSetCommands.class.getMethod("sadd", Object.class, Object[].class), 1);
    methodPool.put(RedisSetCommands.class.getMethod("scard", Object.class), 1);
    methodPool.put(RedisSetCommands.class.getMethod("sdiff", Object[].class), 11);
    methodPool.put(RedisSetCommands.class.getMethod("sdiff", ValueStreamingChannel.class, Object[].class), 12);
    methodPool.put(RedisSetCommands.class.getMethod("sinter", Object[].class), 11);
    methodPool.put(RedisSetCommands.class.getMethod("sinter", ValueStreamingChannel.class, Object[].class), 12);
    methodPool.put(RedisSetCommands.class.getMethod("sismember", Object.class, Object.class), 1);
    methodPool.put(RedisSetCommands.class.getMethod("smembers", Object.class), 1);
    methodPool.put(RedisSetCommands.class.getMethod("smembers", ValueStreamingChannel.class, Object.class), 2);
    methodPool.put(RedisSetCommands.class.getMethod("spop", Object.class), 1);
    methodPool.put(RedisSetCommands.class.getMethod("spop", Object.class, long.class), 1);
    methodPool.put(RedisSetCommands.class.getMethod("srandmember", Object.class), 1);
    methodPool.put(RedisSetCommands.class.getMethod("srandmember", Object.class, long.class), 1);
    methodPool.put(RedisSetCommands.class.getMethod("srandmember", ValueStreamingChannel.class, Object.class, long.class), 2);
    methodPool.put(RedisSetCommands.class.getMethod("srem", Object.class, Object[].class), 1);
    methodPool.put(RedisSetCommands.class.getMethod("sunion", Object[].class), 11);
    methodPool.put(RedisSetCommands.class.getMethod("sunion", ValueStreamingChannel.class, Object[].class), 12);
    methodPool.put(RedisSetCommands.class.getMethod("sunionstore", Object.class, Object[].class), 12);
    methodPool.put(RedisSetCommands.class.getMethod("sscan", Object.class), 1);
    methodPool.put(RedisSetCommands.class.getMethod("sscan", Object.class, ScanArgs.class), 1);
    methodPool.put(RedisSetCommands.class.getMethod("sscan", Object.class, ScanCursor.class, ScanArgs.class), 1);
    methodPool.put(RedisSetCommands.class.getMethod("sscan", Object.class, ScanCursor.class), 1);
    methodPool.put(RedisSetCommands.class.getMethod("sscan", ValueStreamingChannel.class, Object.class), 2);
    methodPool.put(RedisSetCommands.class.getMethod("sscan", ValueStreamingChannel.class, Object.class, ScanArgs.class), 2);
    methodPool.put(RedisSetCommands.class.getMethod("sscan", ValueStreamingChannel.class, Object.class, ScanCursor.class, ScanArgs.class), 2);
    methodPool.put(RedisSetCommands.class.getMethod("sscan", ValueStreamingChannel.class, Object.class, ScanCursor.class), 2);

    ///////////////////////////////////////////////////////////////////////////
    // RedisSortedSetCommands
    ///////////////////////////////////////////////////////////////////////////

    methodPool.put(RedisSortedSetCommands.class.getMethod("zadd", Object.class, double.class, Object.class), 1);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zadd", Object.class, Object[].class), 1);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zadd", Object.class, ScoredValue[].class), 1);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zadd", Object.class, ZAddArgs.class, double.class, Object.class), 1);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zadd", Object.class, ZAddArgs.class, Object[].class), 1);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zadd", Object.class, ZAddArgs.class, ScoredValue[].class), 1);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zaddincr", Object.class, double.class, Object.class), 1);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zaddincr", Object.class, ZAddArgs.class, double.class, Object.class), 1);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zcard", Object.class), 1);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zcount", Object.class, Range.class), 1);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zcount", Object.class, double.class, double.class), 1);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zcount", Object.class, String.class, String.class), 1);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zincrby", Object.class, double.class, Object.class), 1);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zlexcount", Object.class, Range.class), 1);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zlexcount", Object.class, String.class, String.class), 1);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zrange", Object.class, long.class, long.class), 1);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zrange", ValueStreamingChannel.class, Object.class, long.class, long.class), 2);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zrangeWithScores", Object.class, long.class, long.class), 1);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zrangeWithScores", ScoredValueStreamingChannel.class, Object.class, long.class, long.class), 2);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zrangebylex", Object.class, Range.class), 1);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zrangebylex", Object.class, Range.class, Limit.class), 1);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zrangebylex", Object.class, String.class, String.class), 1);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zrangebylex", Object.class, String.class, String.class, long.class, long.class), 1);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zrangebyscore", Object.class, Range.class), 1);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zrangebyscore", Object.class, Range.class, Limit.class), 1);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zrangebyscore", ValueStreamingChannel.class, Object.class, Range.class), 2);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zrangebyscore", ValueStreamingChannel.class, Object.class, Range.class, Limit.class), 2);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zrangebyscore", Object.class, double.class, double.class), 1);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zrangebyscore", Object.class, String.class, String.class), 1);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zrangebyscore", Object.class, double.class, double.class, long.class, long.class), 1);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zrangebyscore", Object.class, String.class, String.class, long.class, long.class), 1);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zrangebyscore", ValueStreamingChannel.class, Object.class, double.class, double.class), 2);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zrangebyscore", ValueStreamingChannel.class, Object.class, String.class, String.class), 2);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zrangebyscore", ValueStreamingChannel.class, Object.class, double.class, double.class, long.class, long.class), 2);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zrangebyscore", ValueStreamingChannel.class, Object.class, String.class, String.class, long.class, long.class), 2);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zrangebyscoreWithScores", Object.class, Range.class), 1);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zrangebyscoreWithScores", Object.class, Range.class, Limit.class), 1);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zrangebyscoreWithScores", ScoredValueStreamingChannel.class, Object.class, Range.class), 2);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zrangebyscoreWithScores", ScoredValueStreamingChannel.class, Object.class, Range.class, Limit.class), 2);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zrangebyscoreWithScores", Object.class, double.class, double.class), 1);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zrangebyscoreWithScores", Object.class, String.class, String.class), 1);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zrangebyscoreWithScores", Object.class, double.class, double.class, long.class, long.class), 1);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zrangebyscoreWithScores", Object.class, String.class, String.class, long.class, long.class), 1);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zrangebyscoreWithScores", ScoredValueStreamingChannel.class, Object.class, double.class, double.class), 2);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zrangebyscoreWithScores", ScoredValueStreamingChannel.class, Object.class, String.class, String.class), 2);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zrangebyscoreWithScores", ScoredValueStreamingChannel.class, Object.class, double.class, double.class, long.class, long.class), 2);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zrangebyscoreWithScores", ScoredValueStreamingChannel.class, Object.class, String.class, String.class, long.class, long.class), 2);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zrank", Object.class, Object.class), 1);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zrem", Object.class, Object[].class), 1);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zremrangebylex", Object.class, Range.class), 1);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zremrangebylex", Object.class, String.class, String.class), 1);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zremrangebyscore", Object.class, Range.class), 1);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zremrangebyscore", Object.class, double.class, double.class), 1);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zremrangebyscore", Object.class, String.class, String.class), 1);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zrevrange", Object.class, long.class, long.class), 1);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zrevrange", ValueStreamingChannel.class, Object.class, long.class, long.class), 2);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zrevrangebylex", Object.class, Range.class), 1);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zrevrangebylex", Object.class, Range.class, Limit.class), 1);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zrevrangeWithScores", Object.class, long.class, long.class), 1);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zrevrangeWithScores", ScoredValueStreamingChannel.class, Object.class, long.class, long.class), 2);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zrevrangebyscore", Object.class, Range.class), 1);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zrevrangebyscore", Object.class, Range.class, Limit.class), 1);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zrevrangebyscore", ValueStreamingChannel.class, Object.class, Range.class), 2);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zrevrangebyscore", ValueStreamingChannel.class, Object.class, Range.class, Limit.class), 2);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zrevrangebyscore", Object.class, double.class, double.class), 1);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zrevrangebyscore", Object.class, String.class, String.class), 1);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zrevrangebyscore", Object.class, double.class, double.class, long.class, long.class), 1);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zrevrangebyscore", Object.class, String.class, String.class, long.class, long.class), 1);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zrevrangebyscore", ValueStreamingChannel.class, Object.class, double.class, double.class), 2);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zrevrangebyscore", ValueStreamingChannel.class, Object.class, String.class, String.class), 2);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zrevrangebyscore", ValueStreamingChannel.class, Object.class, double.class, double.class, long.class, long.class), 2);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zrevrangebyscore", ValueStreamingChannel.class, Object.class, String.class, String.class, long.class, long.class), 2);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zrevrangebyscoreWithScores", Object.class, Range.class), 1);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zrevrangebyscoreWithScores", Object.class, Range.class, Limit.class), 1);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zrevrangebyscoreWithScores", ScoredValueStreamingChannel.class, Object.class, Range.class), 2);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zrevrangebyscoreWithScores", ScoredValueStreamingChannel.class, Object.class, Range.class, Limit.class), 2);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zrevrangebyscoreWithScores", Object.class, double.class, double.class), 1);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zrevrangebyscoreWithScores", Object.class, String.class, String.class), 1);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zrevrangebyscoreWithScores", Object.class, double.class, double.class, long.class, long.class), 1);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zrevrangebyscoreWithScores", Object.class, String.class, String.class, long.class, long.class), 1);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zrevrangebyscoreWithScores", ScoredValueStreamingChannel.class, Object.class, double.class, double.class), 2);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zrevrangebyscoreWithScores", ScoredValueStreamingChannel.class, Object.class, String.class, String.class), 2);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zrevrangebyscoreWithScores", ScoredValueStreamingChannel.class, Object.class, double.class, double.class, long.class, long.class), 2);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zrevrangebyscoreWithScores", ScoredValueStreamingChannel.class, Object.class, String.class, String.class, long.class, long.class), 2);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zrevrank", Object.class, Object.class), 1);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zscan", Object.class), 1);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zscan", Object.class, ScanArgs.class), 1);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zscan", Object.class, ScanCursor.class), 1);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zscan", ScoredValueStreamingChannel.class, Object.class), 2);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zscan", Object.class, ScanCursor.class, ScanArgs.class), 1);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zscan", ScoredValueStreamingChannel.class, Object.class, ScanArgs.class), 2);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zscan", ScoredValueStreamingChannel.class, Object.class, ScanCursor.class), 2);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zscan", ScoredValueStreamingChannel.class, Object.class, ScanCursor.class, ScanArgs.class), 2);
    methodPool.put(RedisSortedSetCommands.class.getMethod("zscore", Object.class, Object.class), 1);


    ///////////////////////////////////////////////////////////////////////////
    // RedisHashAsyncCommands
    ///////////////////////////////////////////////////////////////////////////

    methodPool.put(RedisHashAsyncCommands.class.getMethod("hdel", Object.class, Object[].class), 1);
    methodPool.put(RedisHashAsyncCommands.class.getMethod("hexists", Object.class, Object.class), 1);
    methodPool.put(RedisHashAsyncCommands.class.getMethod("hget", Object.class, Object.class), 1);
    methodPool.put(RedisHashAsyncCommands.class.getMethod("hincrby", Object.class, Object.class, long.class), 1);
    methodPool.put(RedisHashAsyncCommands.class.getMethod("hincrbyfloat", Object.class, Object.class, double.class), 1);
    methodPool.put(RedisHashAsyncCommands.class.getMethod("hgetall", Object.class), 1);
    methodPool.put(RedisHashAsyncCommands.class.getMethod("hgetall", KeyValueStreamingChannel.class, Object.class), 2);
    methodPool.put(RedisHashAsyncCommands.class.getMethod("hkeys", Object.class), 1);
    methodPool.put(RedisHashAsyncCommands.class.getMethod("hkeys", KeyStreamingChannel.class, Object.class), 2);
    methodPool.put(RedisHashAsyncCommands.class.getMethod("hlen", Object.class), 1);
    methodPool.put(RedisHashAsyncCommands.class.getMethod("hmget", Object.class, Object[].class), 1);
    methodPool.put(RedisHashAsyncCommands.class.getMethod("hmget", ValueStreamingChannel.class, Object.class, Object[].class), 2);
    methodPool.put(RedisHashAsyncCommands.class.getMethod("hmset", Object.class, Map.class), 1);
    methodPool.put(RedisHashAsyncCommands.class.getMethod("hscan", Object.class), 1);
    methodPool.put(RedisHashAsyncCommands.class.getMethod("hscan", Object.class, ScanArgs.class), 1);
    methodPool.put(RedisHashAsyncCommands.class.getMethod("hscan", Object.class, ScanCursor.class, ScanArgs.class), 1);
    methodPool.put(RedisHashAsyncCommands.class.getMethod("hscan", Object.class, ScanCursor.class), 1);
    methodPool.put(RedisHashAsyncCommands.class.getMethod("hscan", KeyValueStreamingChannel.class, Object.class), 2);
    methodPool.put(RedisHashAsyncCommands.class.getMethod("hscan", KeyValueStreamingChannel.class, Object.class, ScanArgs.class), 2);
    methodPool.put(RedisHashAsyncCommands.class.getMethod("hscan", KeyValueStreamingChannel.class, Object.class, ScanCursor.class, ScanArgs.class), 2);
    methodPool.put(RedisHashAsyncCommands.class.getMethod("hscan", KeyValueStreamingChannel.class, Object.class, ScanCursor.class), 2);
    methodPool.put(RedisHashAsyncCommands.class.getMethod("hset", Object.class, Object.class, Object.class), 1);
    methodPool.put(RedisHashAsyncCommands.class.getMethod("hsetnx", Object.class, Object.class, Object.class), 1);
    methodPool.put(RedisHashAsyncCommands.class.getMethod("hstrlen", Object.class, Object.class), 1);
    methodPool.put(RedisHashAsyncCommands.class.getMethod("hvals", Object.class), 1);
    methodPool.put(RedisHashAsyncCommands.class.getMethod("hvals", ValueStreamingChannel.class, Object.class), 2);

    ///////////////////////////////////////////////////////////////////////////
    // RedisKeyAsyncCommands
    ///////////////////////////////////////////////////////////////////////////

    methodPool.put(RedisKeyAsyncCommands.class.getMethod("del", Object[].class), 11);
    methodPool.put(RedisKeyAsyncCommands.class.getMethod("unlink", Object[].class), 11);
    methodPool.put(RedisKeyAsyncCommands.class.getMethod("dump", Object.class), 1);
    methodPool.put(RedisKeyAsyncCommands.class.getMethod("exists", Object[].class), 11);
    methodPool.put(RedisKeyAsyncCommands.class.getMethod("expire", Object.class, long.class), 1);
    methodPool.put(RedisKeyAsyncCommands.class.getMethod("expireat", Object.class, Date.class), 1);
    methodPool.put(RedisKeyAsyncCommands.class.getMethod("expireat", Object.class, long.class), 1);
    methodPool.put(RedisKeyAsyncCommands.class.getMethod("move", Object.class, int.class), 1);
    methodPool.put(RedisKeyAsyncCommands.class.getMethod("objectEncoding", Object.class), 1);
    methodPool.put(RedisKeyAsyncCommands.class.getMethod("objectIdletime", Object.class), 1);
    methodPool.put(RedisKeyAsyncCommands.class.getMethod("objectRefcount", Object.class), 1);
    methodPool.put(RedisKeyAsyncCommands.class.getMethod("persist", Object.class), 1);
    methodPool.put(RedisKeyAsyncCommands.class.getMethod("pexpire", Object.class, long.class), 1);
    methodPool.put(RedisKeyAsyncCommands.class.getMethod("pexpireat", Object.class, Date.class), 1);
    methodPool.put(RedisKeyAsyncCommands.class.getMethod("pexpireat", Object.class, long.class), 1);
    methodPool.put(RedisKeyAsyncCommands.class.getMethod("pttl", Object.class), 1);
    methodPool.put(RedisKeyAsyncCommands.class.getMethod("rename", Object.class, Object.class), 1);
    methodPool.put(RedisKeyAsyncCommands.class.getMethod("renamenx", Object.class, Object.class), 1);
    methodPool.put(RedisKeyAsyncCommands.class.getMethod("restore", Object.class, long.class, byte[].class), 1);
    methodPool.put(RedisKeyAsyncCommands.class.getMethod("sort", Object.class), 1);
    methodPool.put(RedisKeyAsyncCommands.class.getMethod("sort", ValueStreamingChannel.class, Object.class), 2);
    methodPool.put(RedisKeyAsyncCommands.class.getMethod("sort", Object.class, SortArgs.class), 1);
    methodPool.put(RedisKeyAsyncCommands.class.getMethod("sort", ValueStreamingChannel.class, Object.class, SortArgs.class), 2);
    methodPool.put(RedisKeyAsyncCommands.class.getMethod("sortStore", Object.class, SortArgs.class, Object.class), 1);
    methodPool.put(RedisKeyAsyncCommands.class.getMethod("touch", Object[].class), 11);
    methodPool.put(RedisKeyAsyncCommands.class.getMethod("ttl", Object.class), 1);
    methodPool.put(RedisKeyAsyncCommands.class.getMethod("type", Object.class), 1);

    ///////////////////////////////////////////////////////////////////////////
    // RedisStringAsyncCommands
    ///////////////////////////////////////////////////////////////////////////

    methodPool.put(RedisStringAsyncCommands.class.getMethod("append", Object.class, Object.class), 1);
    methodPool.put(RedisStringAsyncCommands.class.getMethod("bitcount", Object.class), 1);
    methodPool.put(RedisStringAsyncCommands.class.getMethod("bitcount", Object.class, long.class, long.class), 1);
    methodPool.put(RedisStringAsyncCommands.class.getMethod("bitfield", Object.class, BitFieldArgs.class), 1);
    methodPool.put(RedisStringAsyncCommands.class.getMethod("bitpos", Object.class, boolean.class), 1);
    methodPool.put(RedisStringAsyncCommands.class.getMethod("bitpos", Object.class, boolean.class, long.class, long.class), 1);
    methodPool.put(RedisStringAsyncCommands.class.getMethod("decr", Object.class), 1);
    methodPool.put(RedisStringAsyncCommands.class.getMethod("decrby", Object.class, long.class), 1);
    methodPool.put(RedisStringAsyncCommands.class.getMethod("get", Object.class), 1);
    methodPool.put(RedisStringAsyncCommands.class.getMethod("getbit", Object.class, long.class), 1);
    methodPool.put(RedisStringAsyncCommands.class.getMethod("getrange", Object.class, long.class, long.class), 1);
    methodPool.put(RedisStringAsyncCommands.class.getMethod("getset", Object.class, Object.class), 1);
    methodPool.put(RedisStringAsyncCommands.class.getMethod("incr", Object.class), 1);
    methodPool.put(RedisStringAsyncCommands.class.getMethod("incrby", Object.class, long.class), 1);
    methodPool.put(RedisStringAsyncCommands.class.getMethod("incrbyfloat", Object.class, double.class), 1);
    methodPool.put(RedisStringAsyncCommands.class.getMethod("mget", Object[].class), 11);
    methodPool.put(RedisStringAsyncCommands.class.getMethod("mget", ValueStreamingChannel.class, Object[].class), 12);
    methodPool.put(RedisStringAsyncCommands.class.getMethod("mset", Map.class), 21);
    methodPool.put(RedisStringAsyncCommands.class.getMethod("msetnx", Map.class), 21);
    methodPool.put(RedisStringAsyncCommands.class.getMethod("set", Object.class, Object.class), 1);
    methodPool.put(RedisStringAsyncCommands.class.getMethod("set", Object.class, Object.class, SetArgs.class), 1);
    methodPool.put(RedisStringAsyncCommands.class.getMethod("setbit", Object.class, long.class, int.class), 1);
    methodPool.put(RedisStringAsyncCommands.class.getMethod("setex", Object.class, long.class, Object.class), 1);
    methodPool.put(RedisStringAsyncCommands.class.getMethod("psetex", Object.class, long.class, Object.class), 1);
    methodPool.put(RedisStringAsyncCommands.class.getMethod("setnx", Object.class, Object.class), 1);
    methodPool.put(RedisStringAsyncCommands.class.getMethod("setrange", Object.class, long.class, Object.class), 1);
    methodPool.put(RedisStringAsyncCommands.class.getMethod("strlen", Object.class), 1);

    ///////////////////////////////////////////////////////////////////////////
    // RedisListAsyncCommands
    ///////////////////////////////////////////////////////////////////////////

    methodPool.put(RedisListAsyncCommands.class.getMethod("blpop", long.class, Object[].class), 12);
    methodPool.put(RedisListAsyncCommands.class.getMethod("brpop", long.class, Object[].class), 12);
    methodPool.put(RedisListAsyncCommands.class.getMethod("lindex", Object.class, long.class), 1);
    methodPool.put(RedisListAsyncCommands.class.getMethod("linsert", Object.class, boolean.class, Object.class, Object.class), 1);
    methodPool.put(RedisListAsyncCommands.class.getMethod("llen", Object.class), 1);
    methodPool.put(RedisListAsyncCommands.class.getMethod("lpop", Object.class), 1);
    methodPool.put(RedisListAsyncCommands.class.getMethod("lpush", Object.class, Object[].class), 1);
    methodPool.put(RedisListAsyncCommands.class.getMethod("lpushx", Object.class, Object.class), 1);
    methodPool.put(RedisListAsyncCommands.class.getMethod("lpushx", Object.class, Object[].class), 1);
    methodPool.put(RedisListAsyncCommands.class.getMethod("lrange", Object.class, long.class, long.class), 1);
    methodPool.put(RedisListAsyncCommands.class.getMethod("lrange", ValueStreamingChannel.class, Object.class, long.class, long.class), 2);
    methodPool.put(RedisListAsyncCommands.class.getMethod("lrem", Object.class, long.class, Object.class), 1);
    methodPool.put(RedisListAsyncCommands.class.getMethod("lset", Object.class, long.class, Object.class), 1);
    methodPool.put(RedisListAsyncCommands.class.getMethod("ltrim", Object.class, long.class, long.class), 1);
    methodPool.put(RedisListAsyncCommands.class.getMethod("rpop", Object.class), 1);
    methodPool.put(RedisListAsyncCommands.class.getMethod("rpush", Object.class, Object[].class), 1);
    methodPool.put(RedisListAsyncCommands.class.getMethod("rpushx", Object.class, Object.class), 1);
    methodPool.put(RedisListAsyncCommands.class.getMethod("rpushx", Object.class, Object[].class), 1);

    ///////////////////////////////////////////////////////////////////////////
    // RedisSetAsyncCommands
    ///////////////////////////////////////////////////////////////////////////

    methodPool.put(RedisSetAsyncCommands.class.getMethod("sadd", Object.class, Object[].class), 1);
    methodPool.put(RedisSetAsyncCommands.class.getMethod("scard", Object.class), 1);
    methodPool.put(RedisSetAsyncCommands.class.getMethod("sdiff", Object[].class), 11);
    methodPool.put(RedisSetAsyncCommands.class.getMethod("sdiff", ValueStreamingChannel.class, Object[].class), 12);
    methodPool.put(RedisSetAsyncCommands.class.getMethod("sinter", Object[].class), 11);
    methodPool.put(RedisSetAsyncCommands.class.getMethod("sinter", ValueStreamingChannel.class, Object[].class), 12);
    methodPool.put(RedisSetAsyncCommands.class.getMethod("sismember", Object.class, Object.class), 1);
    methodPool.put(RedisSetAsyncCommands.class.getMethod("smembers", Object.class), 1);
    methodPool.put(RedisSetAsyncCommands.class.getMethod("smembers", ValueStreamingChannel.class, Object.class), 2);
    methodPool.put(RedisSetAsyncCommands.class.getMethod("spop", Object.class), 1);
    methodPool.put(RedisSetAsyncCommands.class.getMethod("spop", Object.class, long.class), 1);
    methodPool.put(RedisSetAsyncCommands.class.getMethod("srandmember", Object.class), 1);
    methodPool.put(RedisSetAsyncCommands.class.getMethod("srandmember", Object.class, long.class), 1);
    methodPool.put(RedisSetAsyncCommands.class.getMethod("srandmember", ValueStreamingChannel.class, Object.class, long.class), 2);
    methodPool.put(RedisSetAsyncCommands.class.getMethod("srem", Object.class, Object[].class), 1);
    methodPool.put(RedisSetAsyncCommands.class.getMethod("sunion", Object[].class), 11);
    methodPool.put(RedisSetAsyncCommands.class.getMethod("sunion", ValueStreamingChannel.class, Object[].class), 12);
    methodPool.put(RedisSetAsyncCommands.class.getMethod("sunionstore", Object.class, Object[].class), 12);
    methodPool.put(RedisSetAsyncCommands.class.getMethod("sscan", Object.class), 1);
    methodPool.put(RedisSetAsyncCommands.class.getMethod("sscan", Object.class, ScanArgs.class), 1);
    methodPool.put(RedisSetAsyncCommands.class.getMethod("sscan", Object.class, ScanCursor.class, ScanArgs.class), 1);
    methodPool.put(RedisSetAsyncCommands.class.getMethod("sscan", Object.class, ScanCursor.class), 1);
    methodPool.put(RedisSetAsyncCommands.class.getMethod("sscan", ValueStreamingChannel.class, Object.class), 2);
    methodPool.put(RedisSetAsyncCommands.class.getMethod("sscan", ValueStreamingChannel.class, Object.class, ScanArgs.class), 2);
    methodPool.put(RedisSetAsyncCommands.class.getMethod("sscan", ValueStreamingChannel.class, Object.class, ScanCursor.class, ScanArgs.class), 2);
    methodPool.put(RedisSetAsyncCommands.class.getMethod("sscan", ValueStreamingChannel.class, Object.class, ScanCursor.class), 2);

    ///////////////////////////////////////////////////////////////////////////
    // RedisSortedSetAsyncCommands
    ///////////////////////////////////////////////////////////////////////////

    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zadd", Object.class, double.class, Object.class), 1);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zadd", Object.class, Object[].class), 1);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zadd", Object.class, ScoredValue[].class), 1);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zadd", Object.class, ZAddArgs.class, double.class, Object.class), 1);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zadd", Object.class, ZAddArgs.class, Object[].class), 1);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zadd", Object.class, ZAddArgs.class, ScoredValue[].class), 1);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zaddincr", Object.class, double.class, Object.class), 1);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zaddincr", Object.class, ZAddArgs.class, double.class, Object.class), 1);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zcard", Object.class), 1);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zcount", Object.class, Range.class), 1);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zcount", Object.class, double.class, double.class), 1);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zcount", Object.class, String.class, String.class), 1);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zincrby", Object.class, double.class, Object.class), 1);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zlexcount", Object.class, Range.class), 1);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zlexcount", Object.class, String.class, String.class), 1);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zrange", Object.class, long.class, long.class), 1);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zrange", ValueStreamingChannel.class, Object.class, long.class, long.class), 2);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zrangeWithScores", Object.class, long.class, long.class), 1);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zrangeWithScores", ScoredValueStreamingChannel.class, Object.class, long.class, long.class), 2);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zrangebylex", Object.class, Range.class), 1);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zrangebylex", Object.class, Range.class, Limit.class), 1);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zrangebylex", Object.class, String.class, String.class), 1);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zrangebylex", Object.class, String.class, String.class, long.class, long.class), 1);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zrangebyscore", Object.class, Range.class), 1);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zrangebyscore", Object.class, Range.class, Limit.class), 1);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zrangebyscore", ValueStreamingChannel.class, Object.class, Range.class), 2);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zrangebyscore", ValueStreamingChannel.class, Object.class, Range.class, Limit.class), 2);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zrangebyscore", Object.class, double.class, double.class), 1);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zrangebyscore", Object.class, String.class, String.class), 1);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zrangebyscore", Object.class, double.class, double.class, long.class, long.class), 1);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zrangebyscore", Object.class, String.class, String.class, long.class, long.class), 1);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zrangebyscore", ValueStreamingChannel.class, Object.class, double.class, double.class), 2);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zrangebyscore", ValueStreamingChannel.class, Object.class, String.class, String.class), 2);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zrangebyscore", ValueStreamingChannel.class, Object.class, double.class, double.class, long.class, long.class), 2);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zrangebyscore", ValueStreamingChannel.class, Object.class, String.class, String.class, long.class, long.class), 2);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zrangebyscoreWithScores", Object.class, Range.class), 1);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zrangebyscoreWithScores", Object.class, Range.class, Limit.class), 1);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zrangebyscoreWithScores", ScoredValueStreamingChannel.class, Object.class, Range.class), 2);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zrangebyscoreWithScores", ScoredValueStreamingChannel.class, Object.class, Range.class, Limit.class), 2);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zrangebyscoreWithScores", Object.class, double.class, double.class), 1);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zrangebyscoreWithScores", Object.class, String.class, String.class), 1);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zrangebyscoreWithScores", Object.class, double.class, double.class, long.class, long.class), 1);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zrangebyscoreWithScores", Object.class, String.class, String.class, long.class, long.class), 1);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zrangebyscoreWithScores", ScoredValueStreamingChannel.class, Object.class, double.class, double.class), 2);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zrangebyscoreWithScores", ScoredValueStreamingChannel.class, Object.class, String.class, String.class), 2);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zrangebyscoreWithScores", ScoredValueStreamingChannel.class, Object.class, double.class, double.class, long.class, long.class), 2);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zrangebyscoreWithScores", ScoredValueStreamingChannel.class, Object.class, String.class, String.class, long.class, long.class), 2);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zrank", Object.class, Object.class), 1);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zrem", Object.class, Object[].class), 1);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zremrangebylex", Object.class, Range.class), 1);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zremrangebylex", Object.class, String.class, String.class), 1);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zremrangebyscore", Object.class, Range.class), 1);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zremrangebyscore", Object.class, double.class, double.class), 1);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zremrangebyscore", Object.class, String.class, String.class), 1);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zrevrange", Object.class, long.class, long.class), 1);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zrevrange", ValueStreamingChannel.class, Object.class, long.class, long.class), 2);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zrevrangebylex", Object.class, Range.class), 1);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zrevrangebylex", Object.class, Range.class, Limit.class), 1);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zrevrangeWithScores", Object.class, long.class, long.class), 1);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zrevrangeWithScores", ScoredValueStreamingChannel.class, Object.class, long.class, long.class), 2);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zrevrangebyscore", Object.class, Range.class), 1);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zrevrangebyscore", Object.class, Range.class, Limit.class), 1);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zrevrangebyscore", ValueStreamingChannel.class, Object.class, Range.class), 2);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zrevrangebyscore", ValueStreamingChannel.class, Object.class, Range.class, Limit.class), 2);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zrevrangebyscore", Object.class, double.class, double.class), 1);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zrevrangebyscore", Object.class, String.class, String.class), 1);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zrevrangebyscore", Object.class, double.class, double.class, long.class, long.class), 1);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zrevrangebyscore", Object.class, String.class, String.class, long.class, long.class), 1);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zrevrangebyscore", ValueStreamingChannel.class, Object.class, double.class, double.class), 2);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zrevrangebyscore", ValueStreamingChannel.class, Object.class, String.class, String.class), 2);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zrevrangebyscore", ValueStreamingChannel.class, Object.class, double.class, double.class, long.class, long.class), 2);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zrevrangebyscore", ValueStreamingChannel.class, Object.class, String.class, String.class, long.class, long.class), 2);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zrevrangebyscoreWithScores", Object.class, Range.class), 1);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zrevrangebyscoreWithScores", Object.class, Range.class, Limit.class), 1);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zrevrangebyscoreWithScores", ScoredValueStreamingChannel.class, Object.class, Range.class), 2);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zrevrangebyscoreWithScores", ScoredValueStreamingChannel.class, Object.class, Range.class, Limit.class), 2);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zrevrangebyscoreWithScores", Object.class, double.class, double.class), 1);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zrevrangebyscoreWithScores", Object.class, String.class, String.class), 1);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zrevrangebyscoreWithScores", Object.class, double.class, double.class, long.class, long.class), 1);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zrevrangebyscoreWithScores", Object.class, String.class, String.class, long.class, long.class), 1);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zrevrangebyscoreWithScores", ScoredValueStreamingChannel.class, Object.class, double.class, double.class), 2);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zrevrangebyscoreWithScores", ScoredValueStreamingChannel.class, Object.class, String.class, String.class), 2);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zrevrangebyscoreWithScores", ScoredValueStreamingChannel.class, Object.class, double.class, double.class, long.class, long.class), 2);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zrevrangebyscoreWithScores", ScoredValueStreamingChannel.class, Object.class, String.class, String.class, long.class, long.class), 2);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zrevrank", Object.class, Object.class), 1);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zscan", Object.class), 1);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zscan", Object.class, ScanArgs.class), 1);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zscan", Object.class, ScanCursor.class), 1);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zscan", ScoredValueStreamingChannel.class, Object.class), 2);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zscan", Object.class, ScanCursor.class, ScanArgs.class), 1);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zscan", ScoredValueStreamingChannel.class, Object.class, ScanArgs.class), 2);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zscan", ScoredValueStreamingChannel.class, Object.class, ScanCursor.class), 2);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zscan", ScoredValueStreamingChannel.class, Object.class, ScanCursor.class, ScanArgs.class), 2);
    methodPool.put(RedisSortedSetAsyncCommands.class.getMethod("zscore", Object.class, Object.class), 1);
  }
}
