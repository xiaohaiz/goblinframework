package org.goblinframework.core.transcoder;

import org.jetbrains.annotations.NotNull;

import java.io.OutputStream;

final public class Transcoder {

  private static final TranscoderEncoderImpl DEFAULT_ENCODER = new TranscoderEncoderImpl();

  @NotNull private final TranscoderSetting setting;
  @NotNull private final TranscoderEncoder encoder;

  Transcoder(@NotNull TranscoderSetting setting) {
    this.setting = setting;
    this.encoder = initializeEncoder();
  }

  public void encode(@NotNull OutputStream outStream, @NotNull Object obj) {
    encoder.encode(setting, outStream, obj);
  }

  private TranscoderEncoder initializeEncoder() {
    return DEFAULT_ENCODER;
  }
}
