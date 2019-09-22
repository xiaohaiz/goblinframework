package org.goblinframework.dao.mysql.module.config

import org.goblinframework.api.service.GoblinManagedBean
import org.goblinframework.api.service.GoblinManagedObject

@GoblinManagedBean(type = "dao.mysql")
class DataSourceConfig
internal constructor(val mapper: DataSourceConfigMapper)
  : GoblinManagedObject(), DataSourceConfigMXBean {

  override fun getDataSourceClassName(): String? {
    return mapper.dataSourceClassName
  }

  override fun getDatabaseName(): String? {
    return mapper.databaseName
  }

  override fun getJdbcUrl(): String? {
    return mapper.jdbcUrl
  }

  override fun getDriverClass(): String? {
    return mapper.driverClass
  }

  override fun getUsername(): String? {
    return mapper.username
  }

  override fun getPassword(): String? {
    throw UnsupportedOperationException()
  }

  override fun getAutoCommit(): Boolean {
    return mapper.autoCommit!!
  }

  override fun getConnectionTimeout(): Long {
    return mapper.connectionTimeout!!
  }

  override fun getIdleTimeout(): Long {
    return mapper.idleTimeout!!
  }

  override fun getMaxLifetime(): Long {
    return mapper.maxLifetime!!
  }

  override fun getConnectionTestQuery(): String? {
    return mapper.connectionTestQuery
  }

  override fun getMinimumIdle(): Int {
    return mapper.minimumIdle!!
  }

  override fun getMaximumPoolSize(): Int {
    return mapper.maximumPoolSize!!
  }

  override fun getPoolName(): String? {
    return mapper.poolName
  }

  override fun getIsolateInternalQueries(): Boolean {
    return mapper.isolateInternalQueries!!
  }

  override fun getAllowPoolSuspension(): Boolean {
    return mapper.allowPoolSuspension!!
  }

  override fun getReadOnly(): Boolean {
    return mapper.readOnly!!
  }

  override fun getRegisterMbeans(): Boolean {
    return mapper.registerMbeans!!
  }

  override fun getCatalog(): String? {
    return mapper.catalog
  }

  override fun getConnectionInitSql(): String? {
    return mapper.connectionInitSql
  }

  override fun getTransactionIsolation(): String? {
    return mapper.transactionIsolation
  }

  override fun getValidationTimeout(): Long {
    return mapper.validationTimeout!!
  }

  override fun getLeakDetectionThreshold(): Long {
    return mapper.leakDetectionThreshold!!
  }

  override fun getMetricRegistry(): String? {
    return mapper.metricRegistry
  }

  override fun getHealthCheckRegistry(): String? {
    return mapper.healthCheckRegistry
  }

  override fun getDataSource(): String? {
    return mapper.dataSource
  }

  override fun getThreadFactory(): String? {
    return mapper.threadFactory
  }
}