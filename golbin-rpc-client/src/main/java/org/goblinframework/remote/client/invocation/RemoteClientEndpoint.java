package org.goblinframework.remote.client.invocation;

public interface RemoteClientEndpoint extends RemoteClientFilter {

  @Override
  default int getOrder() {
    throw new UnsupportedOperationException();
  }
}
