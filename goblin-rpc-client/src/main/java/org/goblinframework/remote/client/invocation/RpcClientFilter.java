package org.goblinframework.remote.client.invocation;

import org.goblinframework.api.function.Ordered;
import org.goblinframework.rpc.filter.RpcFilter;

public interface RpcClientFilter extends RpcFilter<RemoteClientInvocation>, Ordered {
}
