package org.goblinframework.remote.client.invocation.filter

import io.netty.buffer.ByteBufAllocator
import io.netty.buffer.ByteBufOutputStream
import io.netty.buffer.ByteBufUtil
import io.netty.util.ReferenceCountUtil
import org.goblinframework.api.annotation.Singleton
import org.goblinframework.api.core.SerializerMode
import org.goblinframework.core.mapper.JsonMapper
import org.goblinframework.remote.client.invocation.RemoteClientInvocation
import org.goblinframework.remote.client.module.exception.ClientEncodeRequestException
import org.goblinframework.remote.client.module.runtime.RemoteClientTranscoderManager
import org.goblinframework.remote.core.protocol.RemoteRequest
import org.goblinframework.rpc.filter.RpcFilterChain

@Singleton
class EncodeRequestFilter private constructor() : AbstractInternalFilter() {

  companion object {
    @JvmField val INSTANCE = EncodeRequestFilter()
  }

  override fun doFilter(invocation: RemoteClientInvocation, chain: RpcFilterChain<RemoteClientInvocation>) {
    val request = invocation.createRequest()
    try {
      invocation.encodedRequest = encodeRequest(invocation.encoder(), request)
    } catch (ex: Throwable) {
      logger.error("{CLIENT_ENCODE_REQUEST_ERROR} " +
          "Exception raised when encoding request [service={},encoder={}]",
          invocation.serviceId.asText(), invocation.encoder(), ex)
      invocation.complete(null, ClientEncodeRequestException(ex))
      return
    }
    chain.filter(invocation)
  }

  private fun encodeRequest(encoder: SerializerMode?, request: RemoteRequest): ByteArray {
    encoder?.run {
      val transcoder = RemoteClientTranscoderManager.INSTANCE.getTranscoder(this)
      val buf = ByteBufAllocator.DEFAULT.buffer()
      try {
        ByteBufOutputStream(buf).use {
          transcoder.encode(it, request)
          it.flush()
        }
        return ByteBufUtil.getBytes(buf)
      } finally {
        ReferenceCountUtil.release(buf)
      }
    } ?: kotlin.run {
      return JsonMapper.toJson(request).toByteArray(Charsets.UTF_8)
    }
  }
}