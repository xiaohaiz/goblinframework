@file:Suppress("DEPRECATION")

package org.goblinframework.dao.mongo.module.monitor.translator

import com.mongodb.bulk.DeleteRequest
import com.mongodb.bulk.InsertRequest
import com.mongodb.bulk.UpdateRequest
import com.mongodb.bulk.WriteRequest

internal fun translateWriteRequest(request: WriteRequest): String {
  return when (request) {
    is DeleteRequest -> translateDeleteRequest(request)
    is InsertRequest -> translateInsertRequest(request)
    is UpdateRequest -> translateUpdateRequest(request)
    else -> throw UnsupportedOperationException()
  }
}

private fun translateDeleteRequest(request: DeleteRequest): String {
  return "{" +
      "type=${request.type}" +
      "filter=${request.filter}," +
      "isMulti=${request.isMulti}," +
      "collation=${request.collation}" +
      "}"
}

private fun translateInsertRequest(request: InsertRequest): String {
  return "{" +
      "type=${request.type}" +
      "document=${request.document}" +
      "}"
}

private fun translateUpdateRequest(request: UpdateRequest): String {
  var arrayFilters = request.arrayFilters?.joinToString { it.toString() }
  arrayFilters?.run {
    arrayFilters = "[$arrayFilters]"
  }
  return "{" +
      "type=${request.type}" +
      "update=${request.updateValue}," +
      "filter=${request.filter}," +
      "isMulti=${request.isMulti}," +
      "isUpsert=${request.isUpsert}," +
      "collation=${request.collation}" +
      "arrayFilters=$arrayFilters" +
      "}"
}