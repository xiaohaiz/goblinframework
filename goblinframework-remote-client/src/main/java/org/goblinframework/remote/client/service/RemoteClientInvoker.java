package org.goblinframework.remote.client.service;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.goblinframework.api.concurrent.GoblinFuture;
import org.goblinframework.api.core.GoblinException;
import org.goblinframework.core.util.ArrayUtils;
import org.goblinframework.core.util.ReflectionUtils;
import org.goblinframework.remote.client.connection.RemoteConnection;
import org.goblinframework.remote.core.protocol.RemoteRequest;
import org.goblinframework.remote.core.protocol.RemoteResponse;
import org.goblinframework.remote.core.service.RemoteServiceId;
import org.goblinframework.transport.client.flight.MessageFlight;
import org.goblinframework.transport.client.flight.MessageFlightManager;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.Arrays;

public class RemoteClientInvoker implements MethodInterceptor {

  @NotNull private final RemoteServiceId serviceId;
  @NotNull private final RemoteClient client;

  RemoteClientInvoker(@NotNull RemoteServiceId serviceId) {
    this.serviceId = serviceId;
    RemoteClientManager clientManager = RemoteClientManager.INSTANCE;
    this.client = clientManager.getRemoteClient(this.serviceId);
    this.client.getClientFuture().awaitUninterruptibly();
  }

  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    Method method = invocation.getMethod();
    if (ReflectionUtils.isToStringMethod(method)) {
      return "RemoteClientInvoker(" + serviceId + ")";
    }
    Object[] arguments = invocation.getArguments();
    RemoteClientInvokerFuture future = new RemoteClientInvokerFuture();
    RemoteConnection connection = client.availableConnections()
        .stream().findAny().orElse(null);
    if (connection == null) {
      future.complete(null, new GoblinException("No connection"));
    } else {
      MessageFlightManager.INSTANCE.createMessageFlight(true)
          .prepareRequest(requestWriter -> {
            RemoteRequest request = new RemoteRequest();
            request.serviceInterface = serviceId.getInterfaceClass().getName();
            request.serviceGroup = serviceId.getGroup();
            request.serviceVersion = serviceId.getVersion();
            request.methodName = method.getName();
            request.parameterTypes = ArrayUtils.EMPTY_STRING_ARRAY;
            if (method.getParameterCount() > 0) {
              request.parameterTypes = Arrays.stream(method.getParameterTypes())
                  .map(Class::getName).toArray(String[]::new);
            }
            request.returnType = method.getReturnType().getName();
            request.arguments = arguments;
            requestWriter.writePayload(request);
          })
          .sendRequest(connection.getTransportClient())
          .addListener(f -> {
            MessageFlight flight = f.getUninterruptibly();
            RemoteResponse response = (RemoteResponse) flight.responseReader().readPayload();
            Object ret = response.result;
            if (ret instanceof GoblinFuture) {
              future.complete(((GoblinFuture) ret).getUninterruptibly());
            } else {
              future.complete(ret);
            }
          });
    }
    if (method.getReturnType() == GoblinFuture.class) {
      return future;
    } else {
      return future.get();
    }
  }
}
