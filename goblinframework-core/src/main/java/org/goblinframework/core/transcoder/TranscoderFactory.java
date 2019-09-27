package org.goblinframework.core.transcoder;

import org.jetbrains.annotations.NotNull;

@Deprecated
public interface TranscoderFactory {

  @NotNull
  Transcoder1 buildTranscoder(@NotNull TranscoderSetting setting);

}
