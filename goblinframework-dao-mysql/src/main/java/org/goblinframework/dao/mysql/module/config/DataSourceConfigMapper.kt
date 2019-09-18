package org.goblinframework.dao.mysql.module.config

import java.io.Serializable
import java.util.*

class DataSourceConfigMapper : Serializable {

  // ========================================================================
  // General configurations
  // ========================================================================

  /**
   * This is the name of the DataSource class provided by the JDBC driver. Consult the documentation for
   * your specific JDBC driver to get this class name, or see the table below. Note XA data sources are not
   * supported. XA requires a real transaction manager like bitronix. Note that you do not need this property
   * if you are using jdbcUrl for "old-school" DriverManager-based JDBC driver configuration.
   * Default: none
   * For MySQL, the dataSourceClassName is "com.mysql.jdbc.jdbc2.optional.MysqlDataSource"
   * This is HikariCP only.
   */
  var dataSourceClassName: String? = null

  /**
   * In case of using [.dataSourceClassName], the database name must be specified via
   * data source properties.
   * Default: none
   */
  var databaseName: String? = null

  /**
   * This property directs HikariCP to use "DriverManager-based" configuration. We feel that DataSource-based
   * configuration (above) is superior for a variety of reasons (see below), but for many deployments
   * there is little significant difference. When using this property with "old" drivers, you may also need
   * to set the driverClassName property, but try it first without. Note that if this property is used, you
   * may still use DataSource properties to configure your driver and is in fact recommended over driver
   * parameters specified in the URL itself.
   * Default: none
   */
  var jdbcUrl: String? = null

  /**
   * HikariCP will attempt to resolve a driver through the DriverManager based solely on the jdbcUrl,
   * but for some older drivers the driverClassName must also be specified. Omit this property unless
   * you get an obvious error message indicating that the driver was not found.
   * Default: none
   */
  var driverClass: String? = null

  /**
   * This property sets the default authentication username used when obtaining Connections from the underlying
   * driver. Note that for DataSources this works in a very deterministic fashion by calling
   * DataSource.getConnection(*username*, password) on the underlying DataSource. However, for Driver-based
   * configurations, every driver is different. In the case of Driver-based, HikariCP will use this username
   * property to set a user property in the Properties passed to the driver's
   * DriverManager.getConnection(jdbcUrl, props) call. If this is not what you need, skip this method entirely
   * and call addDataSourceProperty("username", ...), for example.
   * Default: none
   */
  var username: String? = null

  /**
   * This property sets the default authentication password used when obtaining Connections from the underlying
   * driver. Note that for DataSources this works in a very deterministic fashion by calling
   * DataSource.getConnection(username, *password*) on the underlying DataSource. However, for Driver-based
   * configurations, every driver is different. In the case of Driver-based, HikariCP will use this password
   * property to set a password property in the Properties passed to the driver's
   * DriverManager.getConnection(jdbcUrl, props) call. If this is not what you need, skip this method entirely
   * and call addDataSourceProperty("pass", ...), for example.
   * Default: none
   */
  var password: String? = null

  // ========================================================================
  // HikariCP specified configurations
  // ========================================================================

  // Frequently used

  /**
   * This property controls the default auto-commit behavior of connections returned from the pool. It is a
   * boolean value.
   * Default: true
   */
  var autoCommit: Boolean? = null

  /**
   * This property controls the maximum number of milliseconds that a client (that's you) will wait for a
   * connection from the pool. If this time is exceeded without a connection becoming available, a SQLException
   * will be thrown. 1000ms is the minimum value.
   * Default: 30000 (30 seconds)
   */
  var connectionTimeout: Long? = null

  /**
   * This property controls the maximum amount of time that a connection is allowed to sit idle in the pool.
   * Whether a connection is retired as idle or not is subject to a maximum variation of +30 seconds, and
   * average variation of +15 seconds. A connection will never be retired as idle before this timeout. A value
   * of 0 means that idle connections are never removed from the pool.
   * Default: 600000 (10 minutes)
   */
  var idleTimeout: Long? = null

  /**
   * This property controls the maximum lifetime of a connection in the pool. When a connection reaches this
   * timeout it will be retired from the pool, subject to a maximum variation of +30 seconds. An in-use
   * connection will never be retired, only when it is closed will it then be removed. We strongly recommend
   * setting this value, and it should be at least 30 seconds less than any database-level connection timeout.
   * A value of 0 indicates no maximum lifetime (infinite lifetime), subject of course to the idleTimeout
   * setting.
   * Default: 1800000 (30 minutes)
   */
  var maxLifetime: Long? = null

  /**
   * If your driver supports JDBC4 we strongly recommend not setting this property. This is for "legacy"
   * databases that do not support the JDBC4 Connection.isValid() API. This is the query that will be executed
   * just before a connection is given to you from the pool to validate that the connection to the database
   * is still alive. Again, try running the pool without this property, HikariCP will log an error if your driver
   * is not JDBC4 compliant to let you know.
   * Default: none
   */
  var connectionTestQuery: String? = null

  /**
   * This property controls the minimum number of idle connections that HikariCP tries to maintain in the pool.
   * If the idle connections dip below this value, HikariCP will make a best effort to add additional connections
   * quickly and efficiently. However, for maximum performance and responsiveness to spike demands, we recommend
   * not setting this value and instead allowing HikariCP to act as a fixed size connection pool.
   * Default: same as maximumPoolSize
   */
  var minimumIdle: Int? = null

  /**
   * This property controls the maximum size that the pool is allowed to reach, including both idle and in-use
   * connections. Basically this value will determine the maximum number of actual connections to the database
   * backend. A reasonable value for this is best determined by your execution environment. When the pool reaches
   * this size, and no idle connections are available, calls to getConnection() will block for up to
   * connectionTimeout milliseconds before timing out.
   * Default: 10
   */
  var maximumPoolSize: Int? = null

  /**
   * This property represents a user-defined name for the connection pool and appears mainly in logging and
   * JMX management consoles to identify pools and pool configurations.
   * Default: auto-generated
   */
  var poolName: String? = null

  // Infrequently used

  /**
   * This property controls whether the pool will "fail fast" if the pool cannot be seeded with initial
   * connections successfully. If you want your application to start even when the database is down/unavailable,
   * set this property to false. Default: true
   */
  var initializationFailFast: Boolean? = null

  /**
   * This property determines whether HikariCP isolates internal pool queries, such as the connection alive test,
   * in their own transaction. Since these are typically read-only queries, it is rarely necessary to encapsulate
   * them in their own transaction. This property only applies if autoCommit is disabled.
   * Default: false
   */
  var isolateInternalQueries: Boolean? = null

  /**
   * This property controls whether the pool can be suspended and resumed through JMX. This is useful for certain
   * failover automation scenarios. When the pool is suspended, calls to getConnection() will not timeout and
   * will be held until the pool is resumed.
   * Default: false
   */
  var allowPoolSuspension: Boolean? = null

  /**
   * This property controls whether Connections obtained from the pool are in read-only mode by default. Note some
   * databases do not support the concept of read-only mode, while others provide query optimizations when the
   * Connection is set to read-only. Whether you need this property or not will depend largely on your application
   * and database.
   * Default: false
   */
  var readOnly: Boolean? = null

  /**
   * This property controls whether or not JMX Management Beans ("MBeans") are registered or not.
   * Default: false
   */
  var registerMbeans: Boolean? = null

  /**
   * This property sets the default catalog for databases that support the concept of catalogs. If this property
   * is not specified, the default catalog defined by the JDBC driver is used.
   * Default: driver default
   */
  var catalog: String? = null

  /**
   * This property sets a SQL statement that will be executed after every new connection creation before adding
   * it to the pool. If this SQL is not valid or throws an exception, it will be treated as a connection failure
   * and the standard retry logic will be followed.
   * Default: none
   */
  var connectionInitSql: String? = null

  /**
   * This property controls the default transaction isolation level of connections returned from the pool. If this
   * property is not specified, the default transaction isolation level defined by the JDBC driver is used. Only
   * use this property if you have specific isolation requirements that are common for all queries. The value of
   * this property is the constant name from the Connection class such as TRANSACTION_READ_COMMITTED,
   * TRANSACTION_REPEATABLE_READ, etc.
   * Default: driver default
   */
  var transactionIsolation: String? = null

  /**
   * This property controls the maximum amount of time that a connection will be tested for aliveness. This value
   * must be less than the connectionTimeout. The lowest accepted validation timeout is 1000ms (1 second).
   * Default: 5000
   */
  var validationTimeout: Long? = null

  /**
   * This property controls the amount of time that a connection can be out of the pool before a message is logged
   * indicating a possible connection leak. A value of 0 means leak detection is disabled. Lowest acceptable
   * value for enabling leak detection is 2000 (2 secs). Default: 0
   */
  var leakDetectionThreshold: Long? = null

  // Statement Cache:
  // Most major database JDBC drivers already have a Statement cache that can be configured (MySQL, PostgreSQL,
  // Derby, etc). A statement cache in the pool would add unneeded weight and no additional functionality. It is
  // simply unnecessary with modern database drivers to implement a cache at the pool level.
  // Log Statement Text / Slow Query Logging:
  // Like Statement caching, most major database vendors support statement logging through properties of their
  // own driver. This includes Oracle, MySQL, Derby, MSSQL, and others. Some even support slow query logging. For
  // those few databases that do not support it, log4jdbc or jdbcdslog-exp are good options.

  // No necessary to configure, via programmatic configuration or IoC container:

  /**
   * This property is only available via programmatic configuration or IoC container. This property allows you to
   * specify an instance of a Codahale/Dropwizard MetricRegistry to be used by the pool to record various metrics.
   * See the Metrics wiki page for details.
   * Default: none
   */
  var metricRegistry: String? = null

  /**
   * This property is only available via programmatic configuration or IoC container. This property allows you to
   * specify an instance of a Codahale/Dropwizard HealthCheckRegistry to be used by the pool to report current
   * health information. See the Health Checks wiki page for details.
   * Default: none
   */
  var healthCheckRegistry: String? = null

  /**
   * This property is only available via programmatic configuration or IoC container. This property allows you to
   * directly set the instance of the DataSource to be wrapped by the pool, rather than having HikariCP construct
   * it via reflection. This can be useful in some dependency injection frameworks. When this property is specified,
   * the dataSourceClassName property and all DataSource-specific properties will be ignored.
   * Default: none
   */
  var dataSource: String? = null

  /**
   * This property is only available via programmatic configuration or IoC container. This property allows you to
   * set the instance of the java.util.concurrent.ThreadFactory that will be used for creating all threads used by
   * the pool. It is needed in some restricted execution environments where threads can only be created through a
   * ThreadFactory provided by the application container.
   * Default: none
   */
  var threadFactory: String? = null

  /**
   * Properties for data source.
   */
  var dataSourceProperties: LinkedHashMap<String, String>? = null

  companion object {
    private const val serialVersionUID = -5837755439301118610L
  }
}
