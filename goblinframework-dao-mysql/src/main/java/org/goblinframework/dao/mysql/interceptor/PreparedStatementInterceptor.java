package org.goblinframework.dao.mysql.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.goblinframework.core.util.ReflectionUtils;
import org.goblinframework.dao.mysql.module.monitor.intruction.MSQ;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;

public class PreparedStatementInterceptor implements MethodInterceptor {

  private static final Map<Method, Integer> methodPool;

  static {
    try {
      methodPool = new LinkedHashMap<>();
      methodPool.put(Statement.class.getMethod("execute", String.class), 1);
      methodPool.put(Statement.class.getMethod("execute", String.class, int.class), 1);
      methodPool.put(Statement.class.getMethod("execute", String.class, int[].class), 1);
      methodPool.put(Statement.class.getMethod("execute", String.class, String[].class), 1);
      methodPool.put(Statement.class.getMethod("executeQuery", String.class), 1);
      methodPool.put(Statement.class.getMethod("executeUpdate", String.class), 1);
      methodPool.put(Statement.class.getMethod("executeUpdate", String.class, int.class), 1);
      methodPool.put(Statement.class.getMethod("executeUpdate", String.class, int[].class), 1);
      methodPool.put(Statement.class.getMethod("executeUpdate", String.class, String[].class), 1);
      methodPool.put(Statement.class.getMethod("executeLargeUpdate", String.class), 1);
      methodPool.put(Statement.class.getMethod("executeLargeUpdate", String.class, int.class), 1);
      methodPool.put(Statement.class.getMethod("executeLargeUpdate", String.class, int[].class), 1);
      methodPool.put(Statement.class.getMethod("executeLargeUpdate", String.class, String[].class), 1);
      methodPool.put(Statement.class.getMethod("executeBatch"), 2);
      methodPool.put(Statement.class.getMethod("executeLargeBatch"), 2);
      methodPool.put(PreparedStatement.class.getMethod("execute"), 3);
      methodPool.put(PreparedStatement.class.getMethod("executeQuery"), 3);
      methodPool.put(PreparedStatement.class.getMethod("executeUpdate"), 3);
      methodPool.put(PreparedStatement.class.getMethod("executeLargeUpdate"), 3);
    } catch (NoSuchMethodException ex) {
      throw new UnsupportedOperationException(ex);
    }
  }

  private final String name;
  private final String mode;
  private final PreparedStatement target;
  private final String sql;

  private final List<String> batchSqlList = new LinkedList<>();

  PreparedStatementInterceptor(@NotNull String name,
                               @NotNull String mode,
                               @NotNull PreparedStatement target,
                               @Nullable String sql) {
    this.name = name;
    this.mode = mode;
    this.target = target;
    this.sql = sql;
  }

  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    Method method = invocation.getMethod();
    if (ReflectionUtils.isToStringMethod(method)) {
      return target.toString();
    }
    Object[] arguments = invocation.getArguments();

    if ("addBatch".equals(method.getName())) {
      String sql = null;
      if (arguments != null && arguments.length != 0) {
        sql = (String) arguments[0];
      }
      Object result = ReflectionUtils.invoke(target, method, arguments);
      if (sql != null) {
        batchSqlList.add(sql);
      }
      return result;
    }
    if ("clearBatch".equals(method.getName())) {
      Object result = ReflectionUtils.invoke(target, method, arguments);
      batchSqlList.clear();
      return result;
    }

    MSQ instruction = null;
    List<String> sqlList = null;
    int methodMode = methodPool.getOrDefault(method, 0);
    switch (methodMode) {
      case 1: {
        instruction = new MSQ();
        if (arguments != null && arguments.length != 0) {
          sqlList = Collections.singletonList((String) arguments[0]);
          instruction.sqlList = sqlList;
        }
        break;
      }
      case 2: {
        instruction = new MSQ();
        if (!this.batchSqlList.isEmpty()) {
          sqlList = Collections.unmodifiableList(this.batchSqlList);
        } else if (sql != null) {
          sqlList = Collections.singletonList(this.sql);
        }
        instruction.sqlList = sqlList;
        break;
      }
      case 3: {
        instruction = new MSQ();
        if (this.sql != null) {
          sqlList = Collections.singletonList(this.sql);
        }
        instruction.sqlList = sqlList;
      }
      default: {
        break;
      }
    }

    if (instruction != null) {
      instruction.name = name;
      instruction.mode = mode;
      instruction.operation = method.getName();
    }
    try {
      return ReflectionUtils.invoke(target, method, arguments);
    } finally {
      if (instruction != null) {
        instruction.close();
      }
    }
  }
}
