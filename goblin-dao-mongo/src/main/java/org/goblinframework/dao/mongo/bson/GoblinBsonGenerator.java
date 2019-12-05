package org.goblinframework.dao.mongo.bson;

import de.undercouch.bson4jackson.BsonConstants;
import de.undercouch.bson4jackson.BsonGenerator;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Extension of {@link BsonGenerator} which providing supports of {@link ObjectId}.
 *
 * @author Xiaohai Zhang
 * @since Sep 27, 2019
 */
final public class GoblinBsonGenerator extends BsonGenerator {

  public GoblinBsonGenerator(int jsonFeatures, int bsonFeatures, OutputStream out) {
    super(jsonFeatures, bsonFeatures, out);
  }

  public void writeObjectId(@NotNull ObjectId objectId) throws IOException {
    _writeArrayFieldNameIfNeeded();
    _verifyValueWrite("write org.bson.types.ObjectId");
    _buffer.putByte(_typeMarker, BsonConstants.TYPE_OBJECTID);
    _buffer.putBytes(objectId.toByteArray());
    flushBuffer();
  }
}
