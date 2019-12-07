package org.goblinframework.database.mysql.persistence

import org.goblinframework.api.dao.GoblinId

/**
 * 只有主键的实体
 */
class UniqueIdEntity {

  @GoblinId(GoblinId.Generator.AUTO_INC)
  var id: Long? = null
}
