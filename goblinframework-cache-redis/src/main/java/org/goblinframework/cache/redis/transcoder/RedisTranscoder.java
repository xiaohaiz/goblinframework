package org.goblinframework.cache.redis.transcoder;

import io.lettuce.core.codec.RedisCodec;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.ByteBufUtil;
import kotlin.text.Charsets;
import org.goblinframework.core.compression.CompressionThreshold;
import org.goblinframework.core.compression.Compressor;
import org.goblinframework.core.exception.GoblinTranscodingException;
import org.goblinframework.core.mbean.GoblinManagedBean;
import org.goblinframework.core.mbean.GoblinManagedObject;
import org.goblinframework.core.serialization.Serializer;
import org.goblinframework.core.transcoder.ByteArrayWrapper;
import org.goblinframework.core.transcoder.DecodeResult;
import org.goblinframework.core.transcoder.Transcoder;
import org.goblinframework.core.transcoder.TranscoderUtils;
import org.goblinframework.core.util.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

@GoblinManagedBean(type = "CORE.REDIS")
final public class RedisTranscoder extends GoblinManagedObject
    implements RedisCodec<String, Object>, RedisTranscoderMXBean {

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
      DecodeResult dr = transcoder.decode(bis);
      if (dr.magic) {
        return dr.result;
      } else {
        if (!(dr.result instanceof byte[])) {
          throw new UnsupportedOperationException();
        }
        byte[] value = (byte[]) dr.result;
        return new String(value, Charsets.UTF_8);
      }
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
      Object using = value;
      if (value instanceof byte[]) {
        using = new ByteArrayWrapper((byte[]) value);
      }
      transcoder.encode(bos, using);
      byte[] bs = ByteBufUtil.getBytes(buf);
      return ByteBuffer.wrap(bs);
    } finally {
      buf.release();
    }
  }

  private byte[] getBytes(ByteBuffer buffer) {
    byte[] bs = new byte[buffer.remaining()];
    buffer.get(bs);
    return bs;
  }

  public void destroy() {
    unregisterIfNecessary();
  }
}
