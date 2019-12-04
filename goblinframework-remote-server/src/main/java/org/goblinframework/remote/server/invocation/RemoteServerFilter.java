package org.goblinframework.remote.server.invocation;

import org.goblinframework.api.function.Ordered;
import org.goblinframework.remote.core.filter.RemoteFilter;

public interface RemoteServerFilter extends RemoteFilter<RemoteServerInvocation>, Ordered {
}
