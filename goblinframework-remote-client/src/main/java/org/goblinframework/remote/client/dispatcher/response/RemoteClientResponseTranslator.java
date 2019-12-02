package org.goblinframework.remote.client.dispatcher.response;

import org.goblinframework.core.service.GoblinManagedBean;
import org.goblinframework.core.service.GoblinManagedLogger;
import org.goblinframework.core.service.GoblinManagedObject;
import org.goblinframework.core.util.MapUtils;
import org.goblinframework.core.util.StringUtils;
import org.goblinframework.remote.client.invocation.RemoteClientInvocation;
import org.goblinframework.remote.client.module.exception.*;
import org.goblinframework.remote.core.module.exception.RemoteException;
import org.goblinframework.remote.core.protocol.RemoteResponse;
import org.goblinframework.remote.core.protocol.RemoteResponseCode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

@GoblinManagedBean(type = "RemoteClient")
@GoblinManagedLogger(name = "goblin.remote.client.response")
public class RemoteClientResponseTranslator extends GoblinManagedObject
    implements RemoteClientResponseTranslatorMXBean {

  /**
   * Return null in case of remote response returns SUCCESS.
   */
  @Nullable
  RemoteException translate(@NotNull RemoteClientInvocation invocation,
                            @NotNull RemoteResponse response) {
    RemoteResponseCode code = RemoteResponseCode.parse(response.code);
    if (code == null) {
      throw new ClientUnknownResponseCodeException(response.code);
    }
    if (code == RemoteResponseCode.SUCCESS) {
      return null;
    }

    String methodText = invocation.asMethodText();
    String serviceText = invocation.serviceId.asText();
    Map<String, Object> extensions = invocation.flight.responseReader().response().extensions;
    String serverHost = MapUtils.getString(extensions, "SERVER_HOST");
    String serverPort = MapUtils.getString(extensions, "SERVER_PORT");
    String serverText = serverHost + ":" + serverPort;
    String exceptionClass = null;
    String exceptionMessage = null;
    if (response.extensions != null) {
      exceptionClass = (String) response.extensions.get("EXCEPTION_CLASS");
      exceptionMessage = (String) response.extensions.get("EXCEPTION_MESSAGE");
    }
    exceptionClass = StringUtils.defaultString(exceptionClass);
    exceptionMessage = StringUtils.defaultIfBlank(exceptionMessage, "NONE");
    String exceptionText = "exceptionClass=" + exceptionClass + ",exceptionMessage=" + exceptionMessage;
    switch (code) {
      case SERVER_DECODE_REQUEST_ERROR: {
        String errMsg = "{SERVER_DECODE_REQUEST_ERROR} raised when invoking [" + methodText + "] " +
            "from [" + serviceText + "] at [" + serverText + "]: " + exceptionText;
        return new ServerDecodeRequestException(errMsg);
      }
      case SERVER_ENCODE_RESPONSE_ERROR: {
        String errMsg = "{SERVER_ENCODE_RESPONSE_ERROR} raised when invoking [" + methodText + "] " +
            "from [" + serviceText + "] at [" + serverText + "]: " + exceptionText;
        return new ServerEncodeResponseException(errMsg);
      }
      case SERVER_BACK_PRESSURE_ERROR: {
        String errMsg = "{SERVER_BACK_PRESSURE_ERROR} raised when invoking [" + methodText + "] " +
            "from [" + serviceText + "] at [" + serverText + "]: " + exceptionText;
        return new ServerBackPressureException(errMsg);
      }
      case SERVER_SERVICE_NOT_FOUND_ERROR: {
        String errMsg = "{SERVER_SERVICE_NOT_FOUND_ERROR} raised when invoking [" + methodText + "] " +
            "from [" + serviceText + "] at [" + serverText + "]: " + exceptionText;
        return new ServerServiceNotFoundException(errMsg);
      }
      case SERVER_METHOD_NOT_FOUND_ERROR: {
        String errMsg = "{SERVER_METHOD_NOT_FOUND_ERROR} raised when invoking [" + methodText + "] " +
            "from [" + serviceText + "] at [" + serverText + "]: " + exceptionText;
        return new ServerMethodNotFoundException(errMsg);
      }
      case SERVER_SERVICE_EXECUTION_ERROR: {
        String errMsg = "{SERVER_SERVICE_EXECUTION_ERROR} raised when invoking [" + methodText + "] " +
            "from [" + serviceText + "] at [" + serverText + "]: " + exceptionText;
        return new ServerServiceExecutionException(errMsg);
      }
      default: {
        throw new UnsupportedOperationException();
      }
    }
  }
}
