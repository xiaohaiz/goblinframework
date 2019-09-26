package org.goblinframework.core.config;

import org.goblinframework.api.config.ConfigParser;
import org.goblinframework.api.core.ThreadSafe;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@ThreadSafe
final class ConfigParserManager {

  private final ReentrantLock lock = new ReentrantLock();
  private final List<ConfigParser> parsers = new ArrayList<>();

  ConfigParserManager() {
  }

  void register(@NotNull ConfigParser parser) {
    lock.lock();
    try {
      parsers.add(parser);
    } finally {
      lock.unlock();
    }
  }

  void parseConfigs() {
    List<ConfigParser> candidates;
    lock.lock();
    try {
      candidates = new ArrayList<>(parsers);
    } finally {
      lock.unlock();
    }
    candidates.parallelStream().forEach(ConfigParser::initialize);
  }
}
