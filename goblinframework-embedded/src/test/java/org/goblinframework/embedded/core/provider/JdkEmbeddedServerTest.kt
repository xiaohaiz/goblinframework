package org.goblinframework.embedded.core.provider

import org.bson.types.ObjectId
import org.goblinframework.embedded.core.EmbeddedServerMode
import org.goblinframework.embedded.core.manager.EmbeddedServerManager
import org.goblinframework.embedded.core.setting.EmbeddedServerSetting
import org.goblinframework.test.runner.GoblinTestRunner
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration

@RunWith(GoblinTestRunner::class)
@ContextConfiguration("/UT.xml")
class JdkEmbeddedServerTest {

  @Test
  fun server() {
    val name = ObjectId().toHexString()
    val setting = EmbeddedServerSetting.builder()
        .name(name)
        .mode(EmbeddedServerMode.JDK)
        .build()
    val server = EmbeddedServerManager.INSTANCE.createServer(setting)
    server.start()
    EmbeddedServerManager.INSTANCE.closeServer(name)
  }
}