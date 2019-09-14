package org.goblinframework.remote.server.expose

data class ExposeServiceId(val interfaceClass: Class<*>,
                           val group: String,
                           val version: String)