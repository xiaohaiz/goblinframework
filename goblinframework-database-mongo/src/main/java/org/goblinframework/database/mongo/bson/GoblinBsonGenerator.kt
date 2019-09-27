package org.goblinframework.database.mongo.bson

import de.undercouch.bson4jackson.BsonConstants
import de.undercouch.bson4jackson.BsonGenerator
import org.bson.types.ObjectId

import java.io.OutputStream

/**
 * [BsonGenerator]的扩展，添加了对[org.bson.types.ObjectId]的支持
 */
class GoblinBsonGenerator(jsonFeatures: Int, bsonFeatures: Int, out: OutputStream)
  : BsonGenerator(jsonFeatures, bsonFeatures, out) {

  fun writeObjectId(objectId: ObjectId) {
    _writeArrayFieldNameIfNeeded()
    _verifyValueWrite("write org.bson.types.ObjectId")
    _buffer.putByte(_typeMarker, BsonConstants.TYPE_OBJECTID)
    _buffer.putBytes(*objectId.toByteArray())
    flushBuffer()
  }
}
