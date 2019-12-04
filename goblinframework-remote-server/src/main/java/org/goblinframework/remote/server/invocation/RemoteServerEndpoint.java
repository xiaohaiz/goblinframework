package org.goblinframework.remote.server.invocation;

public interface RemoteServerEndpoint extends RemoteServerFilter {

  @Override
  default int getOrder() {
    throw new UnsupportedOperationException();
  }
}
