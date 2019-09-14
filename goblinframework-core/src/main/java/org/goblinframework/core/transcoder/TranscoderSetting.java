package org.goblinframework.core.transcoder;

import org.goblinframework.core.compression.Compressor;
import org.goblinframework.core.serialization.Serializer;
import org.jetbrains.annotations.NotNull;

final public class TranscoderSetting {

  public final Compressor compressor;
  public final Serializer serializer;

  TranscoderSetting(@NotNull TranscoderUtils.TranscoderSettingBuilder builder) {
    this.compressor = builder.compressor;
    this.serializer = builder.serializer;
  }
}
