package org.goblinframework.example.database.mysql.entity

import org.goblinframework.api.dao.*
import org.goblinframework.dao.mysql.annotation.GoblinTable
import java.io.Serializable
import java.time.Instant
import java.util.*

@GoblinTable(table = "STATIC_EXAMPLE_DATA")
class StaticExampleData : Serializable {
  companion object {
    private const val serialVersionUID = -4568367284300289600L
  }

  @GoblinId(GoblinId.Generator.AUTO_INC)
  var id: Long? = null
  var name: String? = null
  @CreateTime
  @GoblinField("CT1")
  var createTime1: Instant? = null
  @CreateTime
  @GoblinField("CT2")
  var createTime2: Date? = null
  @UpdateTime
  @GoblinField("UT1")
  var updateTime1: Calendar? = null
  @UpdateTime
  @GoblinField("UT2")
  var updateTime2: Long? = null
  @UpdateTime
  @GoblinField("UT3")
  var updateTime3: String? = null
  @GoblinRevision
  var revision: Int? = null
  @Embed
  var ext: Ext? = null

  class Ext : Serializable {
    companion object {
      private const val serialVersionUID = -4549264762618662638L
    }

    @GoblinField("F1")
    var field1: String? = null
    @GoblinField("F2")
    var field2: String? = null
    @GoblinField("F3")
    var field3: String? = null


  }



}