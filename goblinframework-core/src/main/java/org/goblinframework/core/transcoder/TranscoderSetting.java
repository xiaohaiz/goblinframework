package org.goblinframework.core.transcoder;

import org.goblinframework.core.compression.CompressionThreshold;
import org.goblinframework.core.compression.Compressor;
import org.goblinframework.core.compression.CompressorManager;
import org.goblinframework.core.compression.CompressorMode;
import org.goblinframework.core.serialization.Serializer;
import org.goblinframework.core.serialization.SerializerManager;
import org.goblinframework.core.serialization.SerializerMode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final public class TranscoderSetting {

  public final Compressor compressor;
  public final Serializer serializer;
  public final int compressionThreshold;

  private TranscoderSetting(@NotNull TranscoderSettingBuilder builder) {
    this.compressor = builder.compressor;
    this.serializer = builder.serializer;
    this.compressionThreshold = builder.compressionThreshold == null ? 0 : builder.compressionThreshold.getSize();
  }

  public Transcoder transcoder() {
    return new Transcoder(this);
  }

  public static TranscoderSettingBuilder builder() {
    return new TranscoderSettingBuilder();
  }

  final public static class TranscoderSettingBuilder {
    private Compressor compressor;
    private Serializer serializer;
    private CompressionThreshold compressionThreshold;

    private TranscoderSettingBuilder() {
    }

    @NotNull
    public TranscoderSettingBuilder compressor(@Nullable Compressor compressor) {
      this.compressor = compressor;
      return this;
    }

    @NotNull
    public TranscoderSettingBuilder compressor(@Nullable CompressorMode compressor) {
      Compressor c = null;
      if (compressor != null) {
        c = CompressorManager.INSTANCE.getCompressor(compressor);
      }
      return compressor(c);
    }

    @NotNull
    public TranscoderSettingBuilder serializer(@Nullable Serializer serializer) {
      this.serializer = serializer;
      return this;
    }

    @NotNull
    public TranscoderSettingBuilder serializer(@Nullable SerializerMode serializer) {
      Serializer s = null;
      if (serializer != null) {
        s = SerializerManager.INSTANCE.getSerializer(serializer);
      }
      return serializer(s);
    }

    @NotNull
    public TranscoderSettingBuilder compressionThreshold(@Nullable CompressionThreshold compressionThreshold) {
      this.compressionThreshold = compressionThreshold;
      return this;
    }

    public TranscoderSetting build() {
      return new TranscoderSetting(this);
    }
  }
}
