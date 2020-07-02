package org.goblinframework.example.database.mysql.entity

import org.goblinframework.api.dao.*
import java.io.Serializable
import java.time.Instant
import java.util.*

class StaticExampleData : Serializable {
  companion object {
    private const val serialVersionUID = -4568367284300289600L
  }

  @Id(Id.Generator.AUTO_INC)
  var id: Long? = null
  var name: String? = null
  @CreateTime
  @Field("CT1")
  var createTime1: Instant? = null
  @CreateTime
  @Field("CT2")
  var createTime2: Date? = null
  @UpdateTime
  @Field("UT1")
  var updateTime1: Calendar? = null
  @UpdateTime
  @Field("UT2")
  var updateTime2: Long? = null
  @UpdateTime
  @Field("UT3")
  var updateTime3: String? = null
  @Revision
  var revision: Int? = null
  @Embed
  var ext: Ext? = null

  class Ext : Serializable {
    companion object {
      private const val serialVersionUID = -4549264762618662638L
    }

    @Field("F1")
    var field1: String? = null
    @Field("F2")
    var field2: String? = null
    @Field("F3")
    var field3: String? = null


  }


}