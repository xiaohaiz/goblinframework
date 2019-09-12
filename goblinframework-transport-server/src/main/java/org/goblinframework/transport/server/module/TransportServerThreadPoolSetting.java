package org.goblinframework.transport.server.module;

import org.goblinframework.core.util.SystemUtils;
import org.jetbrains.annotations.NotNull;

final public class TransportServerThreadPoolSetting {

  private final int bossThreads;
  private final int workerThreads;

  private TransportServerThreadPoolSetting(@NotNull TransportServerThreadPoolSettingBuilder builder) {
    this.bossThreads = builder.bossThreads;
    this.workerThreads = builder.workerThreads;
  }

  public int bossThreads() {
    return bossThreads;
  }

  public int workerThreads() {
    return workerThreads;
  }

  @NotNull
  public static TransportServerThreadPoolSettingBuilder builder() {
    return new TransportServerThreadPoolSettingBuilder();
  }

  final public static class TransportServerThreadPoolSettingBuilder {

    private int bossThreads = 1;
    private int workerThreads = SystemUtils.estimateThreads();

    private TransportServerThreadPoolSettingBuilder() {
    }

    @NotNull
    public TransportServerThreadPoolSettingBuilder bossThreads(int bossThreads) {
      this.bossThreads = SystemUtils.estimateThreads(bossThreads);
      return this;
    }

    @NotNull
    public TransportServerThreadPoolSettingBuilder workerThreads(int workerThreads) {
      this.workerThreads = SystemUtils.estimateThreads(workerThreads);
      return this;
    }

    @NotNull
    public TransportServerThreadPoolSetting build() {
      return new TransportServerThreadPoolSetting(this);
    }
  }
}
