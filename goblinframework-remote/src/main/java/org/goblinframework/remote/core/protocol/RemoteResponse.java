package org.goblinframework.remote.core.protocol;

import org.goblinframework.core.util.ExceptionUtils;
import org.goblinframework.core.util.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class RemoteResponse implements Serializable {
  private static final long serialVersionUID = 6015472866822882653L;

  public byte code;
  public Object result;
  public boolean jsonMode;
  public long executionTime;
  public long executionDuration;
  public LinkedHashMap<String, Object> extensions;

  public void writeCode(@NotNull RemoteResponseCode code) {
    this.code = code.getId();
  }

  public void writeError(@NotNull Throwable error) {
    if (extensions == null) {
      extensions = new LinkedHashMap<>();
    }
    String exceptionClass = error.getClass().getName();
    String exceptionMessage = StringUtils.defaultString(error.getMessage());
    String exceptionStackTrace = ExceptionUtils.getStackTrace(error);
    extensions.put("EXCEPTION_CLASS", exceptionClass);
    extensions.put("EXCEPTION_MESSAGE", exceptionMessage);
    extensions.put("EXCEPTION_STACKTRACE", exceptionStackTrace);
  }


  public void resetResult() {
    this.result = null;
    if (this.extensions != null) {
      this.extensions.clear();
    }
  }

  public void resetError() {
    if (this.extensions != null) {
      this.extensions.remove("EXCEPTION_CLASS");
      this.extensions.remove("EXCEPTION_MESSAGE");
      this.extensions.remove("EXCEPTION_STACKTRACE");
    }
  }
}
