package org.goblinframework.database.mysql.persistence

import org.goblinframework.api.dao.CreateTime
import org.goblinframework.api.dao.Id

/**
 * 创建时间字段为[String]
 */
class StringCreateTimeEntity {

  @Id(Id.Generator.AUTO_INC)
  var id: Long? = null

  @CreateTime
  var createTime: String? = null
}