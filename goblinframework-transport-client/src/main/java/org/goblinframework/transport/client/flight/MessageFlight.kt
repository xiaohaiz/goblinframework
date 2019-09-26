package org.goblinframework.transport.client.flight

import org.goblinframework.api.core.Block1
import org.goblinframework.transport.client.channel.TransportClient
import org.goblinframework.transport.core.protocol.*
import org.goblinframework.transport.core.protocol.reader.TransportRequestReader
import org.goblinframework.transport.core.protocol.reader.TransportResponseReader
import org.goblinframework.transport.core.protocol.writer.TransportRequestWriter
import org.goblinframework.transport.core.protocol.writer.TransportResponseWriter
import java.util.concurrent.TimeUnit

class MessageFlight
internal constructor(private val flightManager: MessageFlightManager,
                     response: Boolean) {

  companion object {
    private val MAX_TIMEOUT = TimeUnit.MILLISECONDS.convert(60, TimeUnit.MINUTES)
  }

  private val flightFuture = MessageFlightFuture()
  private val responseReference = TransportResponseReference()
  private val responseReader = TransportResponseReader(responseReference)
  private val requestWriter: TransportRequestWriter

  init {
    val request = TransportRequest()
    request.requestId = TransportRequestId.nextId()
    request.requestCreateTime = System.currentTimeMillis()
    request.response = response
    requestWriter = TransportRequestWriter(request)
  }

  fun prepareRequest(block: Block1<TransportRequestWriter>): MessageFlight {
    block.apply(requestWriter)
    return this
  }

  fun requestWriter(): TransportRequestWriter {
    return requestWriter
  }

  fun responseReader(): TransportResponseReader {
    return responseReader
  }

  fun sendRequest(client: TransportClient): MessageFlightFuture {
    val channel = client.stateChannel()
    if (!channel.available()) {
      val responseWriter = TransportResponseWriter(TransportRequestReader(requestWriter.request()))
      responseWriter.writeException(TransportResponseException(TransportResponseCode.CLIENT_ERROR, "NO_TRANSPORT_CLIENT"))
      flightManager.onResponse(responseWriter.response())
      return flightFuture
    }
    val request = requestWriter.request()
    channel.writeMessage(request)
    if (!request.response) {
      val responseWriter = TransportResponseWriter(TransportRequestReader(requestWriter.request()))
      responseWriter.reset()
      flightManager.onResponse(responseWriter.response())
    }
    return flightFuture
  }

  internal fun id(): Long {
    return requestWriter.request().requestId
  }

  internal fun expired(): Boolean {
    val ct = requestWriter.request().requestCreateTime
    return (System.currentTimeMillis() - ct) >= MAX_TIMEOUT
  }

  internal fun complete(response: TransportResponse) {
    responseReference.set(response)
    flightFuture.complete(this)
  }
}
