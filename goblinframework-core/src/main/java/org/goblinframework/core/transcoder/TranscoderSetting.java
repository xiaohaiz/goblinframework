package org.goblinframework.core.transcoder;

import org.goblinframework.core.compression.Compressor;
import org.goblinframework.core.serialization.Serializer;
import org.jetbrains.annotations.NotNull;

final public class TranscoderSetting {

  public final Compressor compressor;
  public final Serializer serializer;
  public final int compressionThreshold;

  TranscoderSetting(@NotNull TranscoderUtils.TranscoderSettingBuilder builder) {
    this.compressor = builder.compressor;
    this.serializer = builder.serializer;
    this.compressionThreshold = builder.compressionThreshold == null ? 0 : builder.compressionThreshold.getSize();
  }
}
