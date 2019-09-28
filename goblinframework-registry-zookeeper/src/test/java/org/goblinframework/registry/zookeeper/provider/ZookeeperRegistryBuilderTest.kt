package org.goblinframework.registry.zookeeper.provider

import org.goblinframework.registry.core.RegistrySystem
import org.goblinframework.registry.core.manager.RegistryBuilderManager
import org.goblinframework.test.runner.GoblinTestRunner
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration

@RunWith(GoblinTestRunner::class)
@ContextConfiguration("/UT.xml")
class ZookeeperRegistryBuilderTest {

  @Test
  fun getRegistry() {
    val builder = RegistryBuilderManager.INSTANCE.getRegistryBuilder(RegistrySystem.ZKP)
    assertNotNull(builder)
    val registry = builder?.getRegistry("_ut")
    assertNotNull(registry)
  }
}