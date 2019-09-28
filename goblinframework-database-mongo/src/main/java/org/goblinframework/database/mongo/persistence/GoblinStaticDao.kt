package org.goblinframework.database.mongo.persistence

import org.goblinframework.database.mongo.support.MongoClientSupport

abstract class GoblinStaticDao<E, ID> : MongoClientSupport<E, ID>()