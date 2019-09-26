package org.goblinframework.core.transcoder;

import org.goblinframework.api.common.Ordered;
import org.goblinframework.api.core.Install;
import org.jetbrains.annotations.NotNull;

@Install
final public class TranscoderFactoryImpl implements TranscoderFactory, Ordered {

  @Override
  public int getOrder() {
    return LOWEST_PRECEDENCE;
  }

  @NotNull
  @Override
  public Transcoder buildTranscoder(@NotNull TranscoderSetting setting) {
    return new TranscoderImpl(setting);
  }
}
