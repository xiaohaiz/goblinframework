package org.goblinframework.core.transcoder;

import org.jetbrains.annotations.NotNull;

import java.io.OutputStream;

public interface TranscoderEncoder {

  void encode(@NotNull TranscoderSetting setting, @NotNull OutputStream outStream, @NotNull Object obj);

}
