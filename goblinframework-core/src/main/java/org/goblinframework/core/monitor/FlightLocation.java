package org.goblinframework.core.monitor;

import org.goblinframework.api.annotation.ThreadSafe;
import org.goblinframework.core.util.RandomUtils;
import org.goblinframework.core.util.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.*;

final public class FlightLocation {

  private final StartPoint startPoint;
  private final String id;
  private final String clazz;
  private final String method;
  private final LinkedHashMap<String, Object> attributes;

  private FlightLocation(@NotNull FlightLocationBuilder builder) {
    this.startPoint = Objects.requireNonNull(builder.startPoint);

    this.clazz = StringUtils.defaultIfBlank(builder.clazz, "unspecified");

    String methodName = StringUtils.defaultIfBlank(builder.method, "unspecified");
    String[] parameters = builder.parameters;
    String parameterTypes = (parameters == null || parameters.length == 0)
        ? "" : StringUtils.join(parameters, ",");
    this.method = String.format("%s(%s)", methodName, parameterTypes);

    this.id = this.clazz + "::" + this.method;

    this.attributes = builder.attributes;
  }

  @NotNull
  public StartPoint startPoint() {
    return startPoint;
  }

  @NotNull
  public String id() {
    return id;
  }

  @NotNull
  public String clazz() {
    return clazz;
  }

  @NotNull
  public String method() {
    return method;
  }

  @Nullable
  public Object attribute(@NotNull String name) {
    return attributes.get(name);
  }

  @Override
  public String toString() {
    return startPoint + " " + id;
  }

  @NotNull
  public String launch() {
    String flightId = RandomUtils.nextObjectId();
    FlightMonitor monitor = FlightRecorder.getFlightMonitor();
    if (monitor != null) {
      monitor.createFlight(flightId, this);
    }
    return flightId;
  }

  @NotNull
  public static FlightLocationBuilder builder() {
    return new FlightLocationBuilder();
  }

  @ThreadSafe(false)
  final public static class FlightLocationBuilder {

    private StartPoint startPoint;
    private String clazz;
    private String method;
    private String[] parameters;
    private final LinkedHashMap<String, Object> attributes = new LinkedHashMap<>();

    private FlightLocationBuilder() {
    }

    @NotNull
    public FlightLocationBuilder startPoint(@NotNull StartPoint startPoint) {
      this.startPoint = startPoint;
      return this;
    }

    @NotNull
    public FlightLocationBuilder clazz(@NotNull String clazz) {
      this.clazz = clazz.trim();
      return this;
    }

    @NotNull
    public FlightLocationBuilder clazz(@NotNull Class<?> clazz) {
      this.clazz = clazz.getName();
      return this;
    }

    @NotNull
    public FlightLocationBuilder method(@NotNull String method) {
      this.method = method.trim();
      return this;
    }

    @NotNull
    public FlightLocationBuilder method(@NotNull Method method) {
      this.method = method.getName();
      if (this.clazz == null) {
        this.clazz = method.getDeclaringClass().getName();
      }
      if (this.parameters == null) {
        List<String> list = new ArrayList<>();
        for (Class<?> type : method.getParameterTypes()) {
          list.add(type.getName());
        }
        this.parameters = list.toArray(new String[0]);
      }
      return this;
    }

    @NotNull
    public FlightLocationBuilder parameters(@NotNull Class<?>... parameters) {
      this.parameters = Arrays.stream(parameters)
          .filter(Objects::nonNull)
          .map(Class::getName)
          .toArray(String[]::new);
      return this;
    }

    @NotNull
    public FlightLocationBuilder parameters(@NotNull String... parameters) {
      this.parameters = parameters;
      return this;
    }

    @NotNull
    public FlightLocationBuilder attribute(@NotNull String name, @NotNull Object value) {
      attributes.put(name, value);
      return this;
    }

    @NotNull
    public FlightLocation build() {
      return new FlightLocation(this);
    }
  }
}
