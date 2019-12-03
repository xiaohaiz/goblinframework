package org.goblinframework.example.database.service

import org.goblinframework.core.util.JsonUtils
import org.goblinframework.example.database.mysql.dao.StaticExampleDataDao
import org.goblinframework.example.database.mysql.entity.StaticExampleData
import javax.inject.Inject
import javax.inject.Named

@Named
class ExampleService {

  @Inject
  lateinit var staticExampleDataDao: StaticExampleDataDao

  fun staticExampleDataService() {
    val data = StaticExampleData()
    data.name = "static_example"
    data.ext = StaticExampleData.Ext()
    data.ext!!.field1 = "foo"
    data.ext!!.field2 = "bar"
    data.ext!!.field3 = "gee"
    staticExampleDataDao.insert(data)

    val ret = staticExampleDataDao.queryByName(data.name!!)
    println(JsonUtils.toJson(ret))
  }
}