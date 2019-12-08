package org.goblinframework.dao.mongo.persistence

import org.goblinframework.dao.mongo.persistence.internal.MongoPersistenceOperationSupport

abstract class GoblinDynamicDao<E, ID> : MongoPersistenceOperationSupport<E, ID>() {

  init {
    if (!isDynamicDatabase() && !isDynamicCollection()) {
      throw IllegalArgumentException("Neither dynamic database nor dynamic collection found")
    }
  }
}