package org.goblinframework.api.core;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@HashSafe
public class TimeAndUnit implements Serializable {
  private static final long serialVersionUID = 6580751809901154133L;

  public final long time;
  @NotNull public final TimeUnit unit;

  @JsonCreator
  public TimeAndUnit(@JsonProperty("time") long time, @NotNull @JsonProperty("unit") TimeUnit unit) {
    this.time = time;
    this.unit = unit;
  }

  public long toMilliseconds() {
    return TimeUnit.MILLISECONDS.convert(time, unit);
  }

  public long toSeconds() {
    return TimeUnit.SECONDS.convert(time, unit);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    TimeAndUnit that = (TimeAndUnit) o;
    return time == that.time && unit == that.unit;
  }

  @Override
  public int hashCode() {
    return Objects.hash(time, unit);
  }
}
