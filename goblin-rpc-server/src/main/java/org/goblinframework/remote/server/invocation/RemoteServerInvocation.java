package org.goblinframework.remote.server.invocation;

import org.goblinframework.core.serialization.Serializer;
import org.goblinframework.remote.server.service.RemoteService;
import org.goblinframework.rpc.protocol.RpcRequest;
import org.goblinframework.rpc.protocol.RpcResponse;
import org.goblinframework.rpc.protocol.RpcResponseCode;
import org.goblinframework.transport.server.handler.TransportRequestContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;

public class RemoteServerInvocation {

  @NotNull public final TransportRequestContext context;
  @NotNull public final RpcResponse response;
  public RpcRequest request;
  public Serializer serializer;
  public Class<?> interfaceClass;
  public RemoteService service;
  public Method method;
  public Object[] arguments;

  public RemoteServerInvocation(@NotNull TransportRequestContext context) {
    this.context = context;
    this.response = new RpcResponse();
    this.response.writeCode(RpcResponseCode.SUCCESS);
    this.response.extensions = new LinkedHashMap<>();
  }

  public void writeRequest(@NotNull RpcRequest request, @Nullable Serializer serializer) {
    this.request = request;
    this.serializer = serializer;
    this.response.jsonMode = request.jsonMode;
  }

  public void writeError(@NotNull RpcResponseCode code) {
    writeError(code, null);
  }

  public void writeError(@NotNull RpcResponseCode code, @Nullable Throwable error) {
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
