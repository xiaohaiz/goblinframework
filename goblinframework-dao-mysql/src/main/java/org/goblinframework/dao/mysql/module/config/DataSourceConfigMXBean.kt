package org.goblinframework.dao.mysql.module.config

import java.lang.management.PlatformManagedObject

interface DataSourceConfigMXBean : PlatformManagedObject {

  fun getDataSourceClassName(): String?

  fun getDatabaseName(): String?

  fun getJdbcUrl(): String?

  fun getDriverClass(): String?

  fun getUsername(): String?

  fun getPassword(): String?

  fun getAutoCommit(): Boolean

  fun getConnectionTimeout(): Long

  fun getIdleTimeout(): Long

  fun getMaxLifetime(): Long

  fun getConnectionTestQuery(): String?

  fun getMinimumIdle(): Int

  fun getMaximumPoolSize(): Int

  fun getPoolName(): String?

  fun getInitializationFailFast(): Boolean

  fun getIsolateInternalQueries(): Boolean

  fun getAllowPoolSuspension(): Boolean

  fun getReadOnly(): Boolean

  fun getRegisterMbeans(): Boolean

  fun getCatalog(): String?

  fun getConnectionInitSql(): String?

  fun getTransactionIsolation(): String?

  fun getValidationTimeout(): Long

  fun getLeakDetectionThreshold(): Long

  fun getMetricRegistry(): String?

  fun getHealthCheckRegistry(): String?

  fun getDataSource(): String?

  fun getThreadFactory(): String?
}