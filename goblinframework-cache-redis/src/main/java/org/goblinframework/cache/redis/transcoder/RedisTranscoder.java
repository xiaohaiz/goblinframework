package org.goblinframework.cache.redis.transcoder;

import io.lettuce.core.codec.RedisCodec;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufOutputStream;
import kotlin.text.Charsets;
import org.goblinframework.core.compression.CompressionThreshold;
import org.goblinframework.core.compression.Compressor;
import org.goblinframework.core.exception.GoblinTranscodingException;
import org.goblinframework.core.serialization.Serializer;
import org.goblinframework.core.transcoder.Transcoder;
import org.goblinframework.core.transcoder.TranscoderUtils;
import org.goblinframework.core.util.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

final public class RedisTranscoder implements RedisCodec<String, Object> {

  private final Transcoder transcoder;

  public RedisTranscoder(@NotNull Serializer serializer,
                         @Nullable Compressor compressor,
                         @Nullable CompressionThreshold compressionThreshold) {
    this.transcoder = TranscoderUtils.encoder()
        .serializer(serializer)
        .compressor(compressor)
        .compressionThreshold(compressionThreshold)
        .buildTranscoder();
  }

  @Override
  public String decodeKey(ByteBuffer bytes) {
    byte[] bs = getBytes(bytes);
    if (bs.length == 0) {
      return StringUtils.EMPTY;
    }
    return new String(bs, Charsets.UTF_8);
  }

  @Override
  public Object decodeValue(ByteBuffer bytes) {
    byte[] bs = getBytes(bytes);
    try (ByteArrayInputStream bis = new ByteArrayInputStream(bs)) {
      return transcoder.decode(bis).result;
    } catch (IOException ex) {
      throw new GoblinTranscodingException(ex);
    }
  }

  @Override
  public ByteBuffer encodeKey(String key) {
    byte[] bs = key.getBytes(Charsets.UTF_8);
    return ByteBuffer.wrap(bs);
  }

  @Override
  public ByteBuffer encodeValue(Object value) {
    if (value instanceof String) {
      // don't use transcoder in case of string passed in
      byte[] bs = ((String) value).getBytes(Charsets.UTF_8);
      return ByteBuffer.wrap(bs);
    }
    ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
    try {
      ByteBufOutputStream bos = new ByteBufOutputStream(buf);
      transcoder.encode(bos, value);
      ByteBuffer dst = ByteBuffer.allocate(buf.readableBytes());
      buf.readBytes(dst);
      return dst;
    } finally {
      buf.release();
    }
  }

  private byte[] getBytes(ByteBuffer buffer) {
    byte[] bs = new byte[buffer.remaining()];
    buffer.get(bs);
    return bs;
  }
}
