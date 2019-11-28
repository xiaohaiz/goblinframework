package org.goblinframework.core.transcoder;

import kotlin.text.Charsets;
import org.goblinframework.core.compression.Compressor;
import org.goblinframework.core.compression.CompressorManager;
import org.goblinframework.core.serialization.Serializer;
import org.goblinframework.core.serialization.SerializerManager;
import org.goblinframework.core.util.IOUtils;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

final public class Transcoder {

  private static final TranscoderEncoder DEFAULT_ENCODER;

  static {
    DEFAULT_ENCODER = new TranscoderEncoderImpl();
  }

  @NotNull private final TranscoderSetting setting;
  @NotNull private final TranscoderEncoder encoder;

  Transcoder(@NotNull TranscoderSetting setting) {
    this.setting = setting;
    this.encoder = DEFAULT_ENCODER;
  }

  public void encode(@NotNull OutputStream outStream, @NotNull Object obj) {
    encoder.encode(setting, outStream, obj);
  }

  @NotNull
  public static DecodeResult decode(@NotNull InputStream inStream) {
    try {
      return executeDecode(inStream);
    } catch (GoblinTranscoderException ex) {
      throw ex;
    } catch (Exception ex) {
      throw new GoblinTranscoderException(ex);
    }
  }

  private static DecodeResult executeDecode(InputStream inStream) throws Exception {
    if (inStream.available() < 3) {
      // no sufficient bytes for reading, return bytes array directly
      DecodeResult dr = new DecodeResult();
      dr.result = IOUtils.toByteArray(inStream);
      dr.magic = false;
      return dr;
    }
    byte[] bs = new byte[2];
    bs[0] = (byte) inStream.read();
    bs[1] = (byte) inStream.read();
    if (!Arrays.equals(bs, TranscoderConstants.MAGIC_BYTES)) {
      // the first two bytes aren't magic number, return as bytes array
      try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
        bos.write(bs);
        IOUtils.copy(inStream, bos);
        DecodeResult dr = new DecodeResult();
        dr.result = bos.toByteArray();
        dr.magic = false;
        return dr;
      }
    }

    DecodeResult dr = new DecodeResult();
    dr.magic = true;

    byte header = (byte) inStream.read();
    byte compressorId = (byte) (((header & TranscoderConstants.COMPRESSOR_MASK) >> 4) & 0xf);
    Compressor compressor = null;
    if (compressorId != 0) {
      compressor = CompressorManager.INSTANCE.getCompressor(compressorId);
      if (compressor == null) {
        throw new GoblinTranscoderException("Compressor [" + compressorId + "] unrecognized");
      }
    }

    InputStream nextInStream = inStream;
    if (compressor != null) {
      // ok, compressor specified, try to decompress first
      dr.compressor = compressor.mode().getId();
      byte[] decompressed = compressor.decompress(inStream);
      // nextInStream is java.io.ByteArrayInputStream, no necessary to close
      nextInStream = new ByteArrayInputStream(decompressed);
    }

    byte serializerId = (byte) (header & TranscoderConstants.SERIALIZER_MASK);
    Serializer serializer = null;
    if (serializerId != 0) {
      serializer = SerializerManager.INSTANCE.getSerializer(serializerId);
      if (serializer == null) {
        throw new GoblinTranscoderException("Serializer [" + serializerId + "] unrecognized");
      }
    }


    if (serializer == null) {
      // no serializer specified, treat bytes as UTF-8 encoded string
      dr.result = IOUtils.toString(nextInStream, Charsets.UTF_8);
    } else {
      dr.serializer = serializer.mode().getId();
      dr.result = serializer.deserialize(nextInStream);
    }
    if (dr.result instanceof ByteArrayWrapper) {
      dr.result = ((ByteArrayWrapper) dr.result).getValue();
      dr.wrapper = true;
    }
    return dr;
  }

}
