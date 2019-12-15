@file:Suppress("DEPRECATION")

package org.goblinframework.dao.mongo.module.monitor.translator

import com.mongodb.bulk.DeleteRequest
import com.mongodb.bulk.InsertRequest
import com.mongodb.bulk.UpdateRequest

internal fun translateDeleteRequest(request: DeleteRequest): String {
  return "{" +
      "type=${request.type}" +
      "filter=${request.filter}," +
      "isMulti=${request.isMulti}," +
      "collation=${request.collation}" +
      "}"
}

internal fun translateInsertRequest(request: InsertRequest): String {
  return "{" +
      "type=${request.type}" +
      "document=${request.document}" +
      "}"
}

internal fun translateUpdateRequest(request: UpdateRequest): String {
  val arrayFilters = request.arrayFilters?.joinToString { it.toString() }
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