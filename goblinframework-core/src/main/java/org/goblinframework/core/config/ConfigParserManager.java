package org.goblinframework.core.config;

import org.goblinframework.api.common.Singleton;
import org.goblinframework.api.common.ThreadSafe;
import org.goblinframework.api.config.ConfigParser;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@Singleton
@ThreadSafe
final class ConfigParserManager {

  static final ConfigParserManager INSTANCE = new ConfigParserManager();

  private final ReentrantLock lock = new ReentrantLock();
  private final List<ConfigParser> parsers = new ArrayList<>();

  private ConfigParserManager() {
  }

  void register(@NotNull ConfigParser parser) {
    lock.lock();
    try {
      parsers.add(parser);
    } finally {
      lock.unlock();
    }
  }

  void initializeConfigParsers(boolean cleanup) {
    List<ConfigParser> candidates;
    lock.lock();
    try {
      candidates = new ArrayList<>(parsers);
      if (cleanup) {
        parsers.clear();
      }
    } finally {
      lock.unlock();
    }
    candidates.parallelStream().forEach(ConfigParser::initialize);
  }
}
