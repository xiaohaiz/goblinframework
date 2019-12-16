package org.goblinframework.remote.server.invocation.filter

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import org.goblinframework.api.annotation.Singleton
import org.goblinframework.core.mapper.JsonMapper
import org.goblinframework.remote.core.protocol.RemoteResponseCode
import org.goblinframework.remote.server.invocation.RemoteServerInvocation
import org.goblinframework.rpc.filter.RpcFilterChain

@Singleton
class ResolveArgumentFilter private constructor() : AbstractInternalFilter() {

  companion object {
    @JvmField val INSTANCE = ResolveArgumentFilter()
  }

  override fun doFilter(invocation: RemoteServerInvocation, chain: RpcFilterChain<RemoteServerInvocation>) {
    val request = invocation.request
    if (!invocation.request.jsonMode) {
      var arguments = request.arguments
      if (arguments == null) {
        arguments = emptyArray()
      }
      invocation.arguments = arguments
    } else {
      try {
        invocation.arguments = resolveJsonArguments(invocation)
      } catch (ex: Throwable) {
        logger.error("{SERVER_SERVICE_EXECUTION_ERROR} " +
            "Exception raised when resolving arguments [{}]",
            invocation.asText(), ex)
        invocation.writeError(RemoteResponseCode.SERVER_SERVICE_EXECUTION_ERROR, ex)
        return
      }
    }
    chain.filter(invocation)
  }

  private fun resolveJsonArguments(invocation: RemoteServerInvocation): Array<Any?> {
    val request = invocation.request
    if (request.arguments != null && request.arguments.size > 1) {
      throw UnsupportedOperationException("Malformed JSON_ARGUMENTS")
    }
    val json = if (request.arguments.isNullOrEmpty()) {
      JsonMapper.EMPTY_JSON_ARRAY
    } else {
      val value = request.arguments[0] as? String
          ?: throw UnsupportedOperationException("Malformed JSON_ARGUMENTS")
      value
    }
    val tree = JsonMapper.getDefaultObjectMapper().readTree(json)
    if (tree == null || !tree.isArray) {
      throw UnsupportedOperationException("Malformed JSON_ARGUMENTS, json array required")
    }
    val root = tree as ArrayNode
    if (root.size() != invocation.method.parameterCount) {
      throw UnsupportedOperationException("Malformed JSON_ARGUMENTS, parameter count mistake, " +
          "expect is " + invocation.method.parameterCount + " and actual is " + root.size())
    }
    val nodes = mutableListOf<JsonNode>()
    for (node in root) {
      nodes.add(node)
    }
    val genericParameterTypes = invocation.method.genericParameterTypes
    val arguments = arrayOfNulls<Any?>(genericParameterTypes.size)
    val typeFactory = JsonMapper.getDefaultObjectMapper().typeFactory
    for (i in arguments.indices) {
      val javaType = typeFactory.constructType(genericParameterTypes[i])
      arguments[i] = JsonMapper.getDefaultObjectMapper().readValue<Any>(nodes[i].traverse(), javaType)
    }
    return arguments
  }
}