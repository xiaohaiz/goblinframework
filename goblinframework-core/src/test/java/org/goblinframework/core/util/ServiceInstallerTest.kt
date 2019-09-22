package org.goblinframework.core.util

import org.goblinframework.api.service.IServiceInstaller
import org.goblinframework.test.runner.GoblinTestRunner
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration

@RunWith(GoblinTestRunner::class)
@ContextConfiguration("/UT.xml")
class ServiceInstallerTest {

  @Test
  fun serviceInstaller() {
    val installer = IServiceInstaller.instance()
    assertNotNull(installer.firstOrNull(IServiceInstaller::class.java))
  }
}