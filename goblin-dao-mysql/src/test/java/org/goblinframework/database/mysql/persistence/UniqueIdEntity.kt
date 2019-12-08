package org.goblinframework.database.mysql.persistence

import org.goblinframework.api.dao.Id

/**
 * 只有主键的实体
 */
class UniqueIdEntity {

  @Id(Id.Generator.AUTO_INC)
  var id: Long? = null
}
