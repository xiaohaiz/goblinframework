package org.goblinframework.dao.mongo.persistence;

import org.goblinframework.dao.mongo.exception.GoblinMongoPersistenceException;
import org.goblinframework.dao.mongo.persistence.internal.MongoPersistenceOperationSupport;

abstract public class GoblinDynamicDao<E, ID> extends MongoPersistenceOperationSupport<E, ID> {

  protected GoblinDynamicDao() {
    if (!isDynamicDatabase() && !isDynamicCollection()) {
      throw new GoblinMongoPersistenceException("Neither dynamic database nor dynamic collection found");
    }
  }

}
