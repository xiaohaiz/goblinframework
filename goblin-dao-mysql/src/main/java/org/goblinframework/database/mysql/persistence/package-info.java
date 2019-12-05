/**
 * MYSQL persistence:
 * <pre>
 *   +---------+-------+--------------------------------+
 *   | table   | cache | persistence                    |
 *   +---------+-------+--------------------------------+
 *   | static  | no    | GoblinStaticPersistence        |
 *   | static  | yes   | GoblinCachedStaticPersistence  |
 *   | dynamic | no    | GoblinDynamicPersistence       |
 *   | dynamic | yes   | GoblinCachedDynamicPersistence |
 *   +---------+-------+--------------------------------+
 * </pre>
 * MYSQL persistence annotations:
 * <pre>
 *   +--------------------------+-------+----------+---------------------------------------------+
 *   | annotation               | type  | required | description                                 |
 *   +--------------------------+-------+----------+---------------------------------------------+
 *   | GoblinDatabaseConnection | all   | yes      | which database should be connected          |
 *   | GoblinCacheBean(s)       | cache | yes      | define multiple affected caches             |
 *   | GoblinCacheDimension     | cache | yes      | calculate affected dimensions when evicting |
 *   +--------------------------+-------+----------+---------------------------------------------+
 * </pre>
 */
package org.goblinframework.database.mysql.persistence;