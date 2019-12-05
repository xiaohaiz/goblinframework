package org.goblinframework.database.mysql.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.goblinframework.core.util.ReflectionUtils;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class ConnectionInterceptor implements MethodInterceptor {

  private final String name;
  private final String mode;
  private final Connection target;

  ConnectionInterceptor(@NotNull String name,
                        @NotNull String mode,
                        @NotNull Connection target) {
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

    if ("createStatement".equals(method.getName())) {
      Statement statement = (Statement) ReflectionUtils.invoke(target, method, arguments);
      if (statement == null) {
        return null;
      }
      StatementInterceptor interceptor = new StatementInterceptor(name, mode, statement);
      return ReflectionUtils.createProxy(Statement.class, interceptor);
    } else if ("prepareStatement".equals(method.getName())) {
      PreparedStatement statement = (PreparedStatement) ReflectionUtils.invoke(target, method, arguments);
      if (statement == null) {
        return null;
      }
      if (arguments == null || arguments.length == 0) {
        return statement;
      }
      String sql = (String) arguments[0];
      PreparedStatementInterceptor interceptor = new PreparedStatementInterceptor(name, mode, statement, sql);
      return ReflectionUtils.createProxy(PreparedStatement.class, interceptor);
    } else {
      return ReflectionUtils.invoke(target, method, arguments);
    }
  }
}
