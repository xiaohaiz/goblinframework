package org.goblinframework.core.reactor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.goblinframework.core.monitor.FlightExecutor;
import org.goblinframework.core.monitor.FlightRecorder;
import org.goblinframework.core.util.NamedDaemonThreadFactory;
import org.goblinframework.core.util.ProxyUtils;
import org.goblinframework.core.util.ReflectionUtils;
import org.goblinframework.core.util.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.lang.reflect.Method;
import java.util.concurrent.ThreadFactory;

final public class CoreScheduler {
  private static final Logger logger = LoggerFactory.getLogger(CoreScheduler.class);

  private static final Scheduler scheduler;
  private static final Scheduler schedulerProxy;

  static {
    int threadCap = SystemUtils.availableProcessors() * 10;
    int queuedTaskCap = 102400;
    ThreadFactory threadFactory = NamedDaemonThreadFactory.getInstance("CoreScheduler");
    int ttlSeconds = 60;
    scheduler = Schedulers.newBoundedElastic(threadCap, queuedTaskCap, threadFactory, ttlSeconds);
    logger.debug("{CoreScheduler} Core scheduler initialized [threadCap={},queuedTaskCap={},ttlSeconds={}]",
        threadCap, queuedTaskCap, ttlSeconds);

    MethodInterceptor interceptor = new MethodInterceptor() {
      @Override
      public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Method method = methodInvocation.getMethod();
        if (ReflectionUtils.isToStringMethod(method)) {
          return scheduler.toString();
        }
        Object[] arguments = methodInvocation.getArguments();
        String methodName = method.getName();
        if (methodName.equals("schedule") || methodName.equals("schedulePeriodically")) {
          Runnable task = (Runnable) arguments[0];
          if (task != null) {
            FlightExecutor executor = FlightRecorder.currentFlightExecutor();
            Runnable runnable = () -> executor.execute(task::run);
            arguments[0] = runnable;
          }
        }
        return ReflectionUtils.invoke(scheduler, method, arguments);
      }
    };
    schedulerProxy = ProxyUtils.createInterfaceProxy(Scheduler.class, interceptor);
  }

  public static Scheduler getInstance() {
    return schedulerProxy;
  }

  private CoreScheduler() {
  }

  public static void initialize() {
  }

  public static void dispose() {
    scheduler.dispose();
    logger.debug("{CoreScheduler} Core scheduler disposed");
  }
}
