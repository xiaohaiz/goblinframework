package org.goblinframework.core.transcoder;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import org.goblinframework.core.util.ClassUtils;
import org.jetbrains.annotations.NotNull;

import java.io.OutputStream;

final public class Transcoder {

  private static final TranscoderEncoder DEFAULT_ENCODER;

  static {
    boolean netty4Found;
    try {
      ClassUtils.loadClass("io.netty.buffer.ByteBuf");
      netty4Found = true;
    } catch (ClassNotFoundException ex) {
      netty4Found = false;
    }
    if (netty4Found) {
      try {
        DEFAULT_ENCODER = createNetty4TranscoderEncoder();
      } catch (Exception ex) {
        throw new GoblinTranscoderException(ex);
      }
    } else {
      DEFAULT_ENCODER = new TranscoderEncoderImpl();
    }
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

  private static TranscoderEncoder createNetty4TranscoderEncoder() throws Exception {
    ClassPool pool = new ClassPool(true);
    pool.importPackage("com.fasterxml.jackson.databind.ObjectMapper");
    pool.importPackage("io.netty.buffer.ByteBuf");
    pool.importPackage("io.netty.buffer.ByteBufAllocator");
    pool.importPackage("io.netty.buffer.ByteBufInputStream");
    pool.importPackage("io.netty.buffer.ByteBufOutputStream");
    pool.importPackage("io.netty.buffer.ByteBufUtil");
    pool.importPackage("org.goblinframework.core.compression.Compressor");
    pool.importPackage("org.goblinframework.core.serialization.Serializer");
    pool.importPackage("org.goblinframework.core.transcoder.GoblinTranscoderException");
    pool.importPackage("org.goblinframework.core.transcoder.Transcoder");
    pool.importPackage("org.goblinframework.core.transcoder.TranscoderConstants");
    pool.importPackage("org.goblinframework.core.transcoder.TranscoderSetting");
    pool.importPackage("org.goblinframework.core.util.IOUtils");
    pool.importPackage("org.goblinframework.core.util.JsonUtils");
    pool.importPackage("java.io.OutputStream");
    pool.importPackage("java.nio.charset.StandardCharsets");

    CtClass ct = pool.makeClass("org.goblinframework.core.transcoder.TranscoderEncoderNetty4JavassistProxy");
    ct.addInterface(pool.makeInterface("org.goblinframework.core.transcoder.TranscoderEncoder"));

    CtMethod method = CtNewMethod.make("private void executeEncode(TranscoderSetting setting,\n" +
        "                             OutputStream outStream,\n" +
        "                             Object obj) throws Exception {\n" +
        "    if (obj instanceof byte[]) {\n" +
        "      byte[] bs = (byte[]) obj;\n" +
        "      outStream.write(bs);\n" +
        "      return;\n" +
        "    }\n" +
        "\n" +
        "    outStream.write(TranscoderConstants.MAGIC_BYTES);\n" +
        "\n" +
        "    Compressor compressor = setting.compressor;\n" +
        "    Serializer serializer = setting.serializer;\n" +
        "\n" +
        "    ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();\n" +
        "    buf.writeByte(0);   // write header placeholder\n" +
        "    if (obj instanceof String) {\n" +
        "      buf.writeCharSequence((String) obj, StandardCharsets.UTF_8);\n" +
        "    } else if (serializer == null) {\n" +
        "      ObjectMapper mapper = JsonUtils.getDefaultObjectMapper();\n" +
        "      ByteBufOutputStream bos = new ByteBufOutputStream(buf);\n" +
        "      mapper.writeValue((OutputStream) bos, obj);\n" +
        "      bos.flush();\n" +
        "      bos.close();\n" +
        "    } else {\n" +
        "      ByteBufOutputStream bos = new ByteBufOutputStream(buf);\n" +
        "      serializer.serialize(obj, bos);\n" +
        "      bos.flush();\n" +
        "      bos.close();\n" +
        "      // reset header\n" +
        "      buf.setByte(0, serializer.mode().getId());\n" +
        "    }\n" +
        "\n" +
        "    if (compressor == null || setting.compressionThreshold <= 0) {\n" +
        "      ByteBufInputStream bis = new ByteBufInputStream(buf, true);\n" +
        "      try {\n" +
        "        IOUtils.copy(bis, outStream);\n" +
        "      } finally {\n" +
        "        IOUtils.closeStream(bis);\n" +
        "      }\n" +
        "      return;\n" +
        "    }\n" +
        "\n" +
        "    // validate if compression is necessary or not\n" +
        "    int endIdx = buf.writerIndex();\n" +
        "    int actualSize = endIdx - 1;\n" +
        "    if (actualSize < setting.compressionThreshold) {\n" +
        "      ByteBufInputStream bis = new ByteBufInputStream(buf, true);\n" +
        "      try {\n" +
        "        IOUtils.copy(bis, outStream);\n" +
        "      } finally {\n" +
        "        IOUtils.closeStream(bis);\n" +
        "      }\n" +
        "      return;\n" +
        "    }\n" +
        "\n" +
        "    // do compress now\n" +
        "    buf.readerIndex(1);\n" +
        "    ByteBuf data = buf.readBytes(buf.readableBytes());\n" +
        "    ByteBuf compressed = ByteBufAllocator.DEFAULT.buffer();\n" +
        "    try {\n" +
        "      ByteBufInputStream inStream = new ByteBufInputStream(data, true);\n" +
        "      try {\n" +
        "        ByteBufOutputStream bos = new ByteBufOutputStream(compressed);\n" +
        "        compressor.compress(inStream, bos);\n" +
        "        bos.flush();\n" +
        "        bos.close();\n" +
        "      } finally {\n" +
        "        IOUtils.closeStream(inStream);\n" +
        "      }\n" +
        "      int compressedSize = compressed.readableBytes();\n" +
        "      if (compressedSize >= actualSize) {\n" +
        "        // give up compress, size enlarged\n" +
        "        buf.readerIndex(0);\n" +
        "        ByteBufInputStream bis = new ByteBufInputStream(buf, true);\n" +
        "        try {\n" +
        "          IOUtils.copy(bis, outStream);\n" +
        "        } finally {\n" +
        "          IOUtils.closeStream(bis);\n" +
        "        }\n" +
        "      } else {\n" +
        "        buf.setIndex(0, 1);\n" +
        "        buf.writeBytes(ByteBufUtil.getBytes(compressed));\n" +
        "        byte header = buf.getByte(0);\n" +
        "        header = (byte) (header | ((compressor.mode().getId() << 4) & 0xf0));\n" +
        "        buf.setByte(0, header);\n" +
        "        ByteBufInputStream bis = new ByteBufInputStream(buf, true);\n" +
        "        try {\n" +
        "          IOUtils.copy(bis, outStream);\n" +
        "        } finally {\n" +
        "          IOUtils.closeStream(bis);\n" +
        "        }\n" +
        "      }\n" +
        "    } finally {\n" +
        "      compressed.release();\n" +
        "    }\n" +
        "  }", ct);
    ct.addMethod(method);

    method = CtNewMethod.make("public void encode(TranscoderSetting setting, OutputStream outStream, Object obj) {\n" +
        "    try {\n" +
        "      executeEncode(setting, outStream, obj);\n" +
        "      outStream.flush();\n" +
        "    } catch (GoblinTranscoderException ex) {\n" +
        "      throw ex;\n" +
        "    } catch (Exception ex) {\n" +
        "      throw new GoblinTranscoderException(ex);\n" +
        "    }\n" +
        "  }", ct);
    ct.addMethod(method);

    Class<?> clazz = ct.toClass();
    return (TranscoderEncoder) clazz.newInstance();
  }
}
