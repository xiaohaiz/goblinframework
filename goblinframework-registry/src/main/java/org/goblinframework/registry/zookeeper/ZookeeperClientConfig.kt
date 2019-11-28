package org.goblinframework.registry.zookeeper

import org.goblinframework.api.annotation.HashSafe
import org.goblinframework.core.serialization.SerializerMode

@HashSafe
data class ZookeeperClientConfig(val addresses: String,
                                 val connectionTimeout: Int,
                                 val sessionTimeout: Int,
                                 val serializer: SerializerMode)