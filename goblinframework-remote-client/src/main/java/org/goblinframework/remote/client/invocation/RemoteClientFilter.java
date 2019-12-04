package org.goblinframework.remote.client.invocation;

import org.goblinframework.api.function.Ordered;
import org.goblinframework.remote.core.filter.RemoteFilter;

public interface RemoteClientFilter extends RemoteFilter<RemoteClientInvocation>, Ordered {
}
