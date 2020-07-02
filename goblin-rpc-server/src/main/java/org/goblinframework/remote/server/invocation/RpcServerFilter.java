package org.goblinframework.remote.server.invocation;

import org.goblinframework.api.function.Ordered;
import org.goblinframework.rpc.filter.RpcFilter;

public interface RpcServerFilter extends RpcFilter<RemoteServerInvocation>, Ordered {
}
