package org.goblinframework.core.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.goblinframework.api.annotation.HashSafe;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Objects;

@HashSafe
public class HostAndPort implements Serializable {
  private static final long serialVersionUID = 8449206625007131642L;

  @NotNull public final String host;
  public final int port;

  @JsonCreator
  public HostAndPort(@NotNull @JsonProperty("host") String host, @JsonProperty("port") int port) {
    this.host = host;
    this.port = port;
  }

  @Override
  public String toString() {
    return host + ":" + port;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    HostAndPort that = (HostAndPort) o;
    return port == that.port && host.equals(that.host);
  }

  @Override
  public int hashCode() {
    return Objects.hash(host, port);
  }
}
