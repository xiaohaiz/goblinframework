package org.goblinframework.database.mysql.module.test;

import kotlin.text.Charsets;
import org.apache.commons.lang3.mutable.MutableObject;
import org.goblinframework.api.core.GoblinException;
import org.goblinframework.api.core.Singleton;
import org.goblinframework.api.dao.Table;
import org.goblinframework.api.test.TestContext;
import org.goblinframework.api.test.TestExecutionListener;
import org.goblinframework.core.util.ClassUtils;
import org.goblinframework.core.util.IOUtils;
import org.goblinframework.core.util.StringUtils;
import org.goblinframework.database.mysql.client.MysqlClient;
import org.goblinframework.database.mysql.client.MysqlClientManager;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.jdbc.support.JdbcUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Singleton
final public class RebuildMysqlTableBeforeTestMethod implements TestExecutionListener {

  public static final RebuildMysqlTableBeforeTestMethod INSTANCE = new RebuildMysqlTableBeforeTestMethod();

  private RebuildMysqlTableBeforeTestMethod() {
  }

  @Override
  public void beforeTestMethod(@NotNull TestContext testContext) {
    List<RebuildMysqlTable> annotations = lookupAnnotations(testContext);
    if (annotations.isEmpty()) return;
    annotations.forEach(this::executeRebuild);
  }

  private List<RebuildMysqlTable> lookupAnnotations(TestContext testContext) {
    List<RebuildMysqlTable> result = new LinkedList<>();
    Method method = testContext.getTestMethod();
    RebuildMysqlTable s = method.getAnnotation(RebuildMysqlTable.class);
    if (s != null) result.add(s);
    RebuildMysqlTables m = method.getAnnotation(RebuildMysqlTables.class);
    if (m != null) Collections.addAll(result, m.value());
    Class<?> clazz = testContext.getTestClass();
    s = clazz.getAnnotation(RebuildMysqlTable.class);
    if (s != null) result.add(s);
    m = clazz.getAnnotation(RebuildMysqlTables.class);
    if (m != null) Collections.addAll(result, m.value());
    return result;
  }

  private void executeRebuild(RebuildMysqlTable annotation) {
    String tableName = null;
    if (annotation.entity() != Void.class) {
      // Entity class specified
      Class<?> entityClass = annotation.entity();
      Table table = entityClass.getAnnotation(Table.class);
      if (table != null) {
        tableName = table.table();
      }
    } else if (!annotation.table().isEmpty()) {
      // table names specified
      tableName = annotation.table();
    }
    if (tableName == null) {
      // Neither entity nor table specified, ignore
      return;
    }
    tableName = tableName.toUpperCase();

    String path = "/ddl/" + tableName + ".ddl";
    Resource resource = new ClassPathResource(path, ClassUtils.getDefaultClassLoader());
    if (!resource.exists() || !resource.isReadable()) {
      String errMsg = "DDL not available in classpath: %s";
      errMsg = String.format(errMsg, annotation.name());
      throw new GoblinException(errMsg);
    }

    String ddl;
    try (InputStream inStream = resource.getInputStream()) {
      ddl = IOUtils.toString(inStream, Charsets.UTF_8);
    } catch (IOException ex) {
      throw new GoblinException(ex);
    }

    MysqlClientManager clientManager = MysqlClientManager.INSTANCE;
    MysqlClient client = clientManager.getMysqlClient(annotation.name());
    if (client == null) {
      String errMsg = "MysqlClient not found: %s";
      errMsg = String.format(errMsg, annotation.name());
      throw new GoblinException(errMsg);
    }

    if (!StringUtils.contains(tableName, "{}")) {
      dropTable(client, tableName);
      createTable(client, ddl);
    } else {
      if (annotation.range()) {
        for (int i = annotation.from(); i <= annotation.to(); i++) {
          String table = StringUtils.formatMessage(tableName, i);
          dropTable(client, table);
          String script = StringUtils.formatMessage(ddl, i);
          createTable(client, script);
        }
      } else {
        for (String argument : annotation.arguments()) {
          String table = StringUtils.formatMessage(tableName, argument);
          dropTable(client, table);
          String script = StringUtils.formatMessage(ddl, argument);
          createTable(client, script);
        }
      }
    }
  }

  private void dropTable(MysqlClient client, String table) {
    MutableObject<String> sql = new MutableObject<>("DROP TABLE IF EXISTS " + table);
    client.getMasterConnection().getJdbcTemplate().update(sql.getValue());
  }

  private void createTable(MysqlClient client, String script) {
    Resource resource = new ByteArrayResource(script.getBytes(Charsets.UTF_8));
    Connection c = null;
    try {
      c = client.getMasterDataSource().getConnection();
      ScriptUtils.executeSqlScript(c, resource);
    } catch (SQLException ex) {
      throw new GoblinException(ex);
    } finally {
      JdbcUtils.closeConnection(c);
    }
  }
}
