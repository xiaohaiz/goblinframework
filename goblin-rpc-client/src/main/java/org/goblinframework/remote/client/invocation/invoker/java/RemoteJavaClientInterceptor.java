package org.goblinframework.remote.client.invocation.invoker.java;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.goblinframework.core.util.ClassResolver;
import org.goblinframework.core.util.ReflectionUtils;
import org.goblinframework.remote.client.module.exception.ClientMethodNotFoundException;
import org.goblinframework.remote.client.module.runtime.RemoteServiceInformation;
import org.goblinframework.remote.client.module.runtime.RemoteServiceInformationManager;
import org.goblinframework.remote.client.module.runtime.RemoteServiceMethodInformation;
import org.goblinframework.remote.core.service.RemoteServiceId;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

final public class RemoteJavaClientInterceptor implements MethodInterceptor {

  @NotNull private final RemoteServiceId serviceId;
  @NotNull private final RemoteServiceInformation serviceInformation;
  @NotNull private final RemoteJavaClientInvoker invoker;

  RemoteJavaClientInterceptor(@NotNull RemoteServiceId serviceId) {
    this.serviceId = serviceId;
    RemoteServiceInformationManager informationManager = RemoteServiceInformationManager.INSTANCE;
    Class<?> interfaceClass;
    try {
      interfaceClass = ClassResolver.resolve(this.serviceId.getServiceInterface());
    } catch (ClassNotFoundException ex) {
      throw new UnsupportedOperationException(ex);
    }
    this.serviceInformation = informationManager.getRemoteServiceInformation(interfaceClass);
    this.invoker = new RemoteJavaClientInvoker(this.serviceId);
  }

  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    Method method = invocation.getMethod();
    if (ReflectionUtils.isToStringMethod(method)) {
      return "RemoteJavaClientInterceptor->" + invoker.toString();
    }
    RemoteServiceMethodInformation methodInformation = serviceInformation.getRemoteServiceMethodInformation(method);
    if (methodInformation == null) {
      throw new ClientMethodNotFoundException();
    }
    RemoteJavaClientInvocation ctx = new RemoteJavaClientInvocation();
    ctx.serviceId = serviceId;
    ctx.method = method;
    ctx.arguments = invocation.getArguments();
    ctx.methodInformation = methodInformation;
    return invoker.invoke(ctx);
  }
}
