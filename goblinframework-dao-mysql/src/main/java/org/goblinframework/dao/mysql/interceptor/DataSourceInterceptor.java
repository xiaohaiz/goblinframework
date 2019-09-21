package org.goblinframework.dao.mysql.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.goblinframework.core.reflection.ReflectionUtils;
import org.jetbrains.annotations.NotNull;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.sql.Connection;

public class DataSourceInterceptor implements MethodInterceptor {

  private final String name;
  private final String mode;
  private final DataSource target;

  public DataSourceInterceptor(@NotNull String name,
                               @NotNull String mode,
                               @NotNull DataSource target) {
    this.name = name;
    this.mode = mode;
    this.target = target;
  }

  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    Method method = invocation.getMethod();
    if (ReflectionUtils.isToStringMethod(method)) {
      return target.toString();
    }
    Object[] arguments = invocation.getArguments();

    if ("getConnection".equals(method.getName())) {
      Connection connection = (Connection) ReflectionUtils.invoke(target, method, arguments);
      if (connection == null) {
        return null;
      }
      ConnectionInterceptor interceptor = new ConnectionInterceptor(name, mode, connection);
      return ReflectionUtils.createProxy(Connection.class, interceptor);
    }

    return ReflectionUtils.invoke(target, method, arguments);
  }
}
