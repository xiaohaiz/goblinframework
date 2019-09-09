package org.goblinframework.core.util;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.lang.management.PlatformManagedObject;

abstract public class ManagementUtils {
  private static final Logger logger = LoggerFactory.getLogger(ManagementUtils.class);

  public static void registerMBean(@NotNull PlatformManagedObject pmo) {
    ObjectName objectName = pmo.getObjectName();
    if (objectName == null) {
      return;
    }
    try {
      MBeanServer server = ManagementFactory.getPlatformMBeanServer();
      server.registerMBean(pmo, objectName);
    } catch (InstanceAlreadyExistsException ex) {
      logger.trace("MBean '{}' already registered, ignore.", objectName);
    } catch (Exception ignore) {
    }
  }

  public static void unregisterMBean(@NotNull ObjectName objectName) {
    MBeanServer server = ManagementFactory.getPlatformMBeanServer();
    if (!server.isRegistered(objectName)) {
      return;
    }
    try {
      server.unregisterMBean(objectName);
    } catch (InstanceNotFoundException e) {
      logger.trace("MBean '{}' not found, ignore.", objectName);
    } catch (Exception ignore) {
    }
  }
}
