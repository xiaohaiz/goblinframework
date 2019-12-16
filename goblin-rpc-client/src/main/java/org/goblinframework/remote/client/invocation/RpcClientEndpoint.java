package org.goblinframework.remote.client.invocation;

public interface RpcClientEndpoint extends RpcClientFilter {

  @Override
  default int getOrder() {
    throw new UnsupportedOperationException();
  }
}
