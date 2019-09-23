package org.goblinframework.api.monitor;

import org.goblinframework.api.common.ThreadSafe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.*;

final public class FlightLocation {

  private final Flight.StartPoint startPoint;
  private final String id;
  private final String clazz;
  private final String method;
  private final LinkedHashMap<String, Object> attributes;

  private FlightLocation(@NotNull FlightLocationBuilder builder) {
    this.startPoint = Objects.requireNonNull(builder.startPoint);

    this.clazz = (builder.clazz == null || builder.clazz.trim().isEmpty()) ? "unspecified" : builder.clazz;
    String methodName = (builder.method == null || builder.method.trim().isEmpty()) ? "unspecified" : builder.method;
    String parameterTypes = generateParameterTypes(builder.parameters);
    this.method = String.format("%s(%s)", methodName, parameterTypes);

    this.id = this.clazz + "::" + this.method;

    this.attributes = builder.attributes;
  }

  private String generateParameterTypes(String[] parameters) {
    if (parameters == null || parameters.length == 0) {
      return "";
    }
    StringBuilder buf = new StringBuilder();
    for (String parameter : parameters) {
      buf.append(parameter).append(",");
    }
    if (buf.length() > 0) {
      buf.setLength(buf.length() - 1);
    }
    return buf.toString();
  }

  @NotNull
  public Flight.StartPoint startPoint() {
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

  @Nullable
  public FlightId launch() {
    IFlightMonitor monitor = IFlightMonitor.instance();
    if (monitor != null) {
      return monitor.createFlight(this);
    } else {
      return null;
    }
  }

  @NotNull
  public static FlightLocationBuilder builder() {
    return new FlightLocationBuilder();
  }

  @ThreadSafe(false)
  final public static class FlightLocationBuilder {

    private Flight.StartPoint startPoint;
    private String clazz;
    private String method;
    private String[] parameters;
    private final LinkedHashMap<String, Object> attributes = new LinkedHashMap<>();

    private FlightLocationBuilder() {
    }

    @NotNull
    public FlightLocationBuilder startPoint(@NotNull Flight.StartPoint startPoint) {
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
