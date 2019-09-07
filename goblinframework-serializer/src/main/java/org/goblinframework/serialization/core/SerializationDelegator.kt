package org.goblinframework.serialization.core

import org.goblinframework.api.serialization.Serialization
import org.goblinframework.core.management.GoblinManagedBean
import org.goblinframework.core.management.GoblinManagedObject

@GoblinManagedBean("SERIALIZATION", "Serialization")
internal class SerializationDelegator
internal constructor(private val delegator: Serialization)
  : GoblinManagedObject(), Serialization by delegator, SerializationMXBean