package org.goblinframework.core.transcoder;

import org.jetbrains.annotations.NotNull;

public interface TranscoderFactory {

  @NotNull
  Transcoder buildTranscoder(@NotNull TranscoderSetting setting);

}
