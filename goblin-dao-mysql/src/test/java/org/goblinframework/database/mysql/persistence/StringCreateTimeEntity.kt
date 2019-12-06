package org.goblinframework.database.mysql.persistence

import org.goblinframework.api.dao.CreateTime
import org.goblinframework.api.dao.Id
import org.goblinframework.dao.mysql.annotation.GoblinTable

/**
 * 创建时间字段为[String]
 */
@GoblinTable(table = "UT_STRING_CREATE_TIME_T")
class StringCreateTimeEntity {

  @Id(Id.Generator.AUTO_INC)
  var id: Long? = null

  @CreateTime
  var createTime: String? = null
}