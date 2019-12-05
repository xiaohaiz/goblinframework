package org.goblinframework.database.mysql.persistence

import org.goblinframework.api.database.CreateTime
import org.goblinframework.api.database.Id
import org.goblinframework.api.database.Table

/**
 * 创建时间字段为[String]
 */
@Table(table = "UT_STRING_CREATE_TIME_T")
class StringCreateTimeEntity {

  @Id(Id.Generator.AUTO_INC)
  var id: Long? = null

  @CreateTime
  var createTime: String? = null
}