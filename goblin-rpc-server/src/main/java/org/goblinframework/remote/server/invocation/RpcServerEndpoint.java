package org.goblinframework.remote.server.invocation;

public interface RpcServerEndpoint extends RpcServerFilter {

  @Override
  default int getOrder() {
    throw new UnsupportedOperationException();
  }
}
