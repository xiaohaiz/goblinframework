package org.goblinframework.core.transcoder;

import org.jetbrains.annotations.NotNull;

import java.io.OutputStream;

public interface Transcoder {

  void encode(@NotNull OutputStream outStream,
              @NotNull Object obj);

}
