package org.goblinframework.remote.server.service

data class ExposeServiceId(val interfaceClass: Class<*>,
                           val group: String,
                           val version: String)