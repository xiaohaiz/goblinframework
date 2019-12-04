package org.goblinframework.cache.couchbase.transcoder;

import com.couchbase.client.core.lang.Tuple;
import com.couchbase.client.core.lang.Tuple2;
import com.couchbase.client.core.message.ResponseStatus;
import com.couchbase.client.core.message.kv.MutationToken;
import com.couchbase.client.deps.io.netty.buffer.ByteBuf;
import com.couchbase.client.deps.io.netty.buffer.ByteBufOutputStream;
import com.couchbase.client.deps.io.netty.buffer.Unpooled;
import com.couchbase.client.java.document.LegacyDocument;
import com.couchbase.client.java.transcoder.AbstractTranscoder;
import com.couchbase.client.java.transcoder.LegacyTranscoder;
import com.couchbase.client.java.transcoder.TranscoderUtils;
import org.goblinframework.api.core.CompressorMode;
import org.goblinframework.api.core.SerializerMode;
import org.goblinframework.core.compression.CompressionThreshold;
import org.goblinframework.core.compression.Compressor;
import org.goblinframework.core.compression.CompressorManager;
import org.goblinframework.core.serialization.Serializer;
import org.goblinframework.core.serialization.SerializerManager;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.regex.Pattern;

final public class CouchbaseTranscoder extends AbstractTranscoder<LegacyDocument, Object> {

  // General flags
  static final int SERIALIZED = 1;                        // 0000 0000 0000 0000 0000 0000 0000 0001
  static final int FST = 1 << 16;                         // 0000 0000 0000 0001 0000 0000 0000 0000
  static final int HESSIAN2 = 1 << 17;                    // 0000 0000 0000 0010 0000 0000 0000 0000
  static final int COMPRESSED = 2;                        // 0000 0000 0000 0000 0000 0000 0000 0010
  // Special flags for specially handled types.
  private static final int SPECIAL_MASK = 0xff00;         // 0000 0000 0000 0000 1111 1111 0000 0000
  static final int SPECIAL_BOOLEAN = (1 << 8);            // 0000 0000 0000 0000 0000 0001 0000 0000
  static final int SPECIAL_INT = (2 << 8);                // 0000 0000 0000 0000 0000 0010 0000 0000
  static final int SPECIAL_LONG = (3 << 8);               // 0000 0000 0000 0000 0000 0011 0000 0000
  static final int SPECIAL_DATE = (4 << 8);               // 0000 0000 0000 0000 0000 0100 0000 0000
  static final int SPECIAL_BYTE = (5 << 8);               // 0000 0000 0000 0000 0000 0101 0000 0000
  static final int SPECIAL_FLOAT = (6 << 8);              // 0000 0000 0000 0000 0000 0110 0000 0000
  static final int SPECIAL_DOUBLE = (7 << 8);             // 0000 0000 0000 0000 0000 0111 0000 0000
  static final int SPECIAL_BYTEARRAY = (8 << 8);          // 0000 0000 0000 0000 0000 1000 0000 0000

  @NotNull private final Serializer serializer;
  @NotNull private final CompressionThreshold compressionThreshold;

  public CouchbaseTranscoder(@NotNull SerializerMode serializer, @NotNull CompressionThreshold compressionThreshold) {
    this.serializer = SerializerManager.INSTANCE.getSerializer(serializer);
    this.compressionThreshold = compressionThreshold;
  }

  @Override
  protected LegacyDocument doDecode(String id, ByteBuf content, long cas, int expiry, int flags, ResponseStatus status) throws Exception {
    return null;
  }

  @Override
  protected Tuple2<ByteBuf, Integer> doEncode(LegacyDocument document) throws Exception {
    int flags = 0;
    Object content = document.content();

    boolean isJson = false;
    ByteBuf encoded;
    if (content instanceof String) {
      String c = (String) content;
      isJson = isJsonObject(c);
      encoded = TranscoderUtils.encodeStringAsUtf8(c);
    } else {
      encoded = Unpooled.buffer();

      if (content instanceof Long) {
        flags |= SPECIAL_LONG;
        encoded.writeBytes(LegacyTranscoder.encodeNum((Long) content, 8));
      } else if (content instanceof Integer) {
        flags |= SPECIAL_INT;
        encoded.writeBytes(LegacyTranscoder.encodeNum((Integer) content, 4));
      } else if (content instanceof Boolean) {
        flags |= SPECIAL_BOOLEAN;
        boolean b = (Boolean) content;
        encoded = Unpooled.buffer().writeByte(b ? '1' : '0');
      } else if (content instanceof Date) {
        flags |= SPECIAL_DATE;
        encoded.writeBytes(LegacyTranscoder.encodeNum(((Date) content).getTime(), 8));
      } else if (content instanceof Byte) {
        flags |= SPECIAL_BYTE;
        encoded.writeByte((Byte) content);
      } else if (content instanceof Float) {
        flags |= SPECIAL_FLOAT;
        encoded.writeBytes(LegacyTranscoder.encodeNum(Float.floatToRawIntBits((Float) content), 4));
      } else if (content instanceof Double) {
        flags |= SPECIAL_DOUBLE;
        encoded.writeBytes(LegacyTranscoder.encodeNum(Double.doubleToRawLongBits((Double) content), 8));
      } else if (content instanceof byte[]) {
        flags |= SPECIAL_BYTEARRAY;
        encoded.writeBytes((byte[]) content);
      } else {
        switch (serializer.mode()) {
          case JAVA: {
            flags |= SERIALIZED;
            break;
          }
          case FST: {
            flags |= FST;
            break;
          }
          case HESSIAN2: {
            flags |= HESSIAN2;
            break;
          }
          default:
            throw new UnsupportedOperationException();
        }
        ByteBufOutputStream bos = new ByteBufOutputStream(encoded);
        serializer.serialize(content, bos);
        bos.flush();
        bos.close();
      }
    }

    if (compressionThreshold != CompressionThreshold.NONE && !isJson && encoded.readableBytes() >= compressionThreshold.getSize()) {
      Compressor compressor = CompressorManager.INSTANCE.getCompressor(CompressorMode.GZIP);
      byte[] compressed = compressor.compress(encoded.copy().array());
      if (compressed.length < encoded.array().length) {
        encoded.clear().writeBytes(compressed);
        flags |= COMPRESSED;
      }
    }

    return Tuple.create(encoded, flags);
  }

  @Override
  @Deprecated
  public LegacyDocument newDocument(String id, int expiry, Object content, long cas) {
    return LegacyDocument.create(id, expiry, content, cas);
  }

  @Override
  public LegacyDocument newDocument(String id, int expiry, Object content, long cas, MutationToken mutationToken) {
    return LegacyDocument.create(id, expiry, content, cas, mutationToken);
  }

  @Override
  public Class<LegacyDocument> documentType() {
    return LegacyDocument.class;
  }

  private static final Pattern DECIMAL_PATTERN = Pattern.compile("^-?\\d+$");

  private static boolean isJsonObject(final String s) {
    if (s == null || s.isEmpty()) {
      return false;
    }
    return s.startsWith("{") || s.startsWith("[")
        || "true".equals(s) || "false".equals(s)
        || "null".equals(s) || DECIMAL_PATTERN.matcher(s).matches();
  }
}
