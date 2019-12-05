package org.goblinframework.database.mongo.bson

import com.fasterxml.jackson.core.JsonEncoding
import de.undercouch.bson4jackson.BsonFactory
import de.undercouch.bson4jackson.BsonGenerator
import java.io.OutputStream

/**
 * [BsonFactory]的扩展，创建[GoblinBsonGenerator]代替原来的实例
 */
class GoblinBsonFactory : BsonFactory() {

  override fun createGenerator(out: OutputStream, enc: JsonEncoding?): BsonGenerator {
    var using = out
    val ctx = _createContext(using, true)
    ctx.encoding = enc
    if (enc == JsonEncoding.UTF8 && _outputDecorator != null) {
      using = _outputDecorator.decorate(ctx, using)
    }
    val generator = GoblinBsonGenerator(_generatorFeatures, _bsonGeneratorFeatures, using)
    val codec = codec
    if (codec != null) {
      generator.codec = codec
    }
    if (_characterEscapes != null) {
      generator.characterEscapes = _characterEscapes
    }
    return generator
  }
}
