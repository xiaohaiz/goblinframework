package org.goblinframework.database.mysql.persistence

import org.goblinframework.api.dao.Id
import org.goblinframework.api.dao.Table

/**
 * 只有主键的实体
 */
@Table(table = "UT_UNIQUE_ID_T")
class UniqueIdEntity {

  @Id(Id.Generator.AUTO_INC)
  var id: Long? = null
}
