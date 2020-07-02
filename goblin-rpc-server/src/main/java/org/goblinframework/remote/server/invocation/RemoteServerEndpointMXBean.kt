package org.goblinframework.remote.server.invocation

interface RemoteServerEndpointMXBean : RemoteServerFilterMXBean {

  fun getSuccessCount(): Long

  fun getFailureCount(): Long

}