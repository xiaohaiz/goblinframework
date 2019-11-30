package org.goblinframework.remote.server.invocation;

import org.goblinframework.remote.core.protocol.RemoteResponse;
import org.goblinframework.remote.core.protocol.RemoteResponseCode;
import org.goblinframework.transport.server.handler.TransportRequestContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;

public class RemoteServerInvocation {

  @NotNull public final TransportRequestContext context;
  @NotNull public final RemoteResponse response;

  public RemoteServerInvocation(@NotNull TransportRequestContext context) {
    this.context = context;
    this.response = new RemoteResponse();
    this.response.writeCode(RemoteResponseCode.SUCCESS);
    this.response.extensions = new LinkedHashMap<>();
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
}
