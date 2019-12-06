package org.goblinframework.dao.mongo.persistence

import org.goblinframework.dao.mongo.persistence.internal.MongoPersistenceSupport

abstract class GoblinDynamicDao<E, ID> : MongoPersistenceSupport<E, ID>() {
}