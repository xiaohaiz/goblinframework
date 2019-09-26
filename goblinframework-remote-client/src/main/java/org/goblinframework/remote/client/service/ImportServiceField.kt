package org.goblinframework.remote.client.service

import org.goblinframework.api.remote.ImportService
import org.goblinframework.core.util.GoblinField

class ImportServiceField
internal constructor(private val bean: Any,
                     private val field: GoblinField,
                     private val annotation: ImportService) {

  internal fun inject() {}
}