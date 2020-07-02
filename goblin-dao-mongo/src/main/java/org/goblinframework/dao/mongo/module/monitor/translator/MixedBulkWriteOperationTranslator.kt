package org.goblinframework.dao.mongo.module.monitor.translator

import com.mongodb.operation.MixedBulkWriteOperation

@Suppress("DEPRECATION")
class MixedBulkWriteOperationTranslator
internal constructor(private val operation: MixedBulkWriteOperation)
  : OperationTranslator {

  override fun translate(): String {
    val writeRequests = operation.writeRequests?.run {
      "[" + this.joinToString { translateWriteRequest(it) } + "]"
    }
    return "namespace=${operation.namespace}" +
        " writeRequests=${writeRequests}" +
        " ordered=${operation.isOrdered}" +
        " retryWrites=${operation.retryWrites}" +
        " writeConcern=${translateWriteConcern(operation.writeConcern)}" +
        " bypassDocumentValidation=${operation.bypassDocumentValidation}"
  }
}