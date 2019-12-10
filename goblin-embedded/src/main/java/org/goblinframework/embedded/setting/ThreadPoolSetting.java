package org.goblinframework.embedded.setting;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

final public class ThreadPoolSetting {

  private final int corePoolSize;
  private final int maximumPoolSize;
  private final long keepAliveTime;
  private final TimeUnit unit;

  private ThreadPoolSetting(@NotNull ThreadPoolSettingBuilder builder) {
    this.corePoolSize = builder.corePoolSize;
    this.maximumPoolSize = builder.maximumPoolSize;
    this.keepAliveTime = builder.keepAliveTime;
    this.unit = Objects.requireNonNull(builder.unit);
  }

  public int corePoolSize() {
    return corePoolSize;
  }

  public int maximumPoolSize() {
    return maximumPoolSize;
  }

  public long keepAliveTime() {
    return keepAliveTime;
  }

  @NotNull
  public TimeUnit unit() {
    return unit;
  }

  @NotNull
  public static ThreadPoolSettingBuilder builder() {
    return new ThreadPoolSettingBuilder();
  }

  final public static class ThreadPoolSettingBuilder {

    private int corePoolSize = 4;
    private int maximumPoolSize = 32;
    private long keepAliveTime = 1;
    private TimeUnit unit = TimeUnit.MINUTES;

    private ThreadPoolSettingBuilder() {
    }

    @NotNull
    public ThreadPoolSettingBuilder corePoolSize(int corePoolSize) {
      this.corePoolSize = corePoolSize;
      return this;
    }

    @NotNull
    public ThreadPoolSettingBuilder maximumPoolSize(int maximumPoolSize) {
      this.maximumPoolSize = maximumPoolSize;
      return this;
    }

    @NotNull
    public ThreadPoolSettingBuilder setKeepAliveTime(long keepAliveTime, @NotNull TimeUnit unit) {
      this.keepAliveTime = keepAliveTime;
      this.unit = unit;
      return this;
    }

    @NotNull
    public ThreadPoolSetting build() {
      return new ThreadPoolSetting(this);
    }
  }
}
