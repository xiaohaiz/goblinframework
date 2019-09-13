package org.goblinframework.core.transcoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import kotlin.text.Charsets;
import org.goblinframework.core.exception.GoblinTranscodingException;
import org.goblinframework.core.serialization.Serializer;
import org.goblinframework.core.serialization.SerializerManager;
import org.goblinframework.core.util.IOUtils;
import org.goblinframework.core.util.JsonUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

abstract public class Transcoder {

  public static void encode(@NotNull OutputStream outStream,
                            @NotNull Object obj,
                            @Nullable Serializer serializer) {
    try {
      doEncode(outStream, obj, serializer);
      outStream.flush();
    } catch (GoblinTranscodingException ex) {
      throw ex;
    } catch (Exception ex) {
      throw new GoblinTranscodingException(ex);
    }
  }

  @NotNull
  public static DecodedObject decode(@NotNull InputStream inStream) {
    try {
      return doDecode(inStream);
    } catch (GoblinTranscodingException ex) {
      throw ex;
    } catch (Exception ex) {
      throw new GoblinTranscodingException(ex);
    }
  }

  private static void doEncode(@NotNull OutputStream outStream,
                               @NotNull Object obj,
                               @Nullable Serializer serializer) throws Exception {
    if (obj instanceof byte[]) {
      byte[] bs = (byte[]) obj;
      outStream.write(bs);
      return;
    }

    outStream.write(TranscoderConstants.MAGIC_BYTES);

    if (obj instanceof String) {
      outStream.write(0);
      byte[] bs = ((String) obj).getBytes(Charsets.UTF_8);
      outStream.write(bs);
      return;
    }

    if (serializer == null) {
      outStream.write(0);
      ObjectMapper mapper = JsonUtils.getDefaultObjectMapper();
      mapper.writeValue(outStream, obj);
    } else {
      byte header = serializer.mode().getId();
      outStream.write(header);
      serializer.serialize(obj, outStream);
    }
  }

  @NotNull
  private static DecodedObject doDecode(@NotNull InputStream inStream) throws Exception {
    if (inStream.available() < 3) {
      return new DecodedObject(IOUtils.toByteArray(inStream), (byte) -1);
    }
    byte[] bs = new byte[2];
    bs[0] = (byte) inStream.read();
    bs[1] = (byte) inStream.read();
    if (!Arrays.equals(bs, TranscoderConstants.MAGIC_BYTES)) {
      try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
        bos.write(bs);
        IOUtils.copy(inStream, bos);
        return new DecodedObject(bos.toByteArray(), (byte) -1);
      }
    }
    byte header = (byte) inStream.read();
    byte serializerId = (byte) (header & TranscoderConstants.SERIALIZER_MASK);
    if (serializerId == 0) {
      return new DecodedObject(IOUtils.toString(inStream, Charsets.UTF_8), serializerId);
    }
    Serializer serializer = SerializerManager.INSTANCE.getSerializer(serializerId);
    if (serializer == null) {
      throw new GoblinTranscodingException("Serializer [" + serializerId + "] not found");
    }
    return new DecodedObject(serializer.deserialize(inStream), serializerId);
  }

}
