package org.goblinframework.remote.server.invocation;

import org.goblinframework.core.serialization.Serializer;
import org.goblinframework.remote.core.protocol.RemoteRequest;
import org.goblinframework.remote.core.protocol.RemoteResponse;
import org.goblinframework.remote.core.protocol.RemoteResponseCode;
import org.goblinframework.remote.server.service.RemoteService;
import org.goblinframework.transport.server.handler.TransportRequestContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;

public class RemoteServerInvocation {

  @NotNull public final TransportRequestContext context;
  @NotNull public final RemoteResponse response;
  public RemoteRequest request;
  public Serializer serializer;
  public Class<?> interfaceClass;
  public RemoteService service;
  public Method method;

  public RemoteServerInvocation(@NotNull TransportRequestContext context) {
    this.context = context;
    this.response = new RemoteResponse();
    this.response.writeCode(RemoteResponseCode.SUCCESS);
    this.response.extensions = new LinkedHashMap<>();
  }

  public void writeRequest(@NotNull RemoteRequest request, @Nullable Serializer serializer) {
    this.request = request;
    this.serializer = serializer;
    this.response.jsonMode = request.jsonMode;
  }

  public void writeError(@NotNull RemoteResponseCode code) {
    writeError(code, null);
  }

  public void writeError(@NotNull RemoteResponseCode code, @Nullable Throwable error) {
    response.writeCode(code);
    if (error != null) {
      response.writeError(error);
    }
  }

  @NotNull
  public String asText() {
    if (request == null) {
      return "client=" + context.asClientText();
    } else {
      return "client=" + context.asClientText() + ",service=" + request.serviceInterface + "/" + request.serviceVersion;
    }
  }
}
