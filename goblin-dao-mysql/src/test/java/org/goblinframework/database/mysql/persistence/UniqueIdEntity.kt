package org.goblinframework.database.mysql.persistence

import org.goblinframework.api.dao.Id
import org.goblinframework.dao.mysql.annotation.GoblinTable

/**
 * 只有主键的实体
 */
@GoblinTable(table = "UT_UNIQUE_ID_T")
class UniqueIdEntity {

  @Id(Id.Generator.AUTO_INC)
  var id: Long? = null
}
