package org.goblinframework.dao.mongo.module.management

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.dao.mongo.client.MongoClientManager
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping

@Singleton
@RequestMapping("/goblin/dao/mongo")
class MongoDaoManagement private constructor() {

  companion object {
    @JvmField val INSTANCE = MongoDaoManagement()
  }

  @RequestMapping("index.do")
  fun index(model: Model): String {
    model.addAttribute("mongoClientManager", MongoClientManager.INSTANCE)
    return "goblin/dao/mongo/index"
  }
}