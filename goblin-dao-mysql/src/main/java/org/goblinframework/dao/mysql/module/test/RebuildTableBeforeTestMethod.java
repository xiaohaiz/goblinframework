package org.goblinframework.dao.mysql.module.test;

import org.goblinframework.api.annotation.Singleton;
import org.goblinframework.api.test.TestContext;
import org.goblinframework.api.test.TestExecutionListener;
import org.goblinframework.core.util.ClassUtils;
import org.goblinframework.core.util.IOUtils;
import org.goblinframework.core.util.StringUtils;
import org.goblinframework.dao.mysql.exception.GoblinMysqlConfigException;
import org.goblinframework.dao.mysql.exception.GoblinMysqlPersistenceException;
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
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Singleton
final public class RebuildTableBeforeTestMethod implements TestExecutionListener {

  private static final RebuildTableBeforeTestMethod instance = new RebuildTableBeforeTestMethod();

  @NotNull
  public static RebuildTableBeforeTestMethod getInstance() {
    return instance;
  }

  private RebuildTableBeforeTestMethod() {
  }

  @Override
  public void beforeTestMethod(@NotNull TestContext testContext) {
    List<RebuildTable> annotations = lookupAnnotations(testContext);
    if (annotations.isEmpty()) {
      return;
    }
    for (RebuildTable annotation : annotations) {
      generateCandidates(annotation).forEach(it -> {
        String sql = "DROP TABLE IF EXISTS " + it.table;
        it.client.getMasterConnection().getJdbcTemplate().update(sql);

        Resource resource = new ByteArrayResource(it.script.getBytes(StandardCharsets.UTF_8));
        Connection c = null;
        try {
          c = it.client.getMasterDataSource().getConnection();
          ScriptUtils.executeSqlScript(c, resource);
        } catch (SQLException ex) {
          throw new GoblinMysqlPersistenceException(ex);
        } finally {
          JdbcUtils.closeConnection(c);
        }
      });
    }
  }

  private List<RebuildTable> lookupAnnotations(TestContext testContext) {
    List<RebuildTable> result = new ArrayList<>();
    Method method = testContext.getTestMethod();
    RebuildTable s = method.getAnnotation(RebuildTable.class);
    if (s != null) result.add(s);
    RebuildTables m = method.getAnnotation(RebuildTables.class);
    if (m != null) Collections.addAll(result, m.value());
    Class<?> clazz = testContext.getTestClass();
    s = clazz.getAnnotation(RebuildTable.class);
    if (s != null) result.add(s);
    m = clazz.getAnnotation(RebuildTables.class);
    if (m != null) Collections.addAll(result, m.value());
    return result;
  }

  private List<Candidate> generateCandidates(RebuildTable annotation) {
    String connection = annotation.connection();
    MysqlClientManager clientManager = MysqlClientManager.INSTANCE;
    MysqlClient client = clientManager.getMysqlClient(connection);
    if (client == null) {
      throw new GoblinMysqlConfigException("MYSQL connection not found: " + connection);
    }
    String tableName = annotation.table().toUpperCase();
    String path = "/META-INF/goblin/ddl/" + tableName + ".ddl";
    Resource resource = new ClassPathResource(path, ClassUtils.getDefaultClassLoader());
    if (!resource.exists() || !resource.isReadable()) {
      throw new GoblinMysqlConfigException("DDL not found in classpath: " + tableName + ".ddl");
    }
    String ddl;
    try (InputStream inStream = resource.getInputStream()) {
      ddl = IOUtils.toString(inStream, StandardCharsets.UTF_8);
    } catch (IOException ex) {
      throw new UnsupportedOperationException(ex);
    }
    List<Candidate> candidates = new ArrayList<>();
    RebuildTableScope scope = annotation.scope();
    if (scope.enable()) {
      RebuildTableParameter[] parameters = scope.parameters();
      if (parameters.length > 0) {
        for (RebuildTableParameter parameter : parameters) {
          Object[] objs = new Object[parameter.parameters().length];
          for (int i = 0; i < parameter.parameters().length; i++) {
            objs[i] = parameter.parameters()[i].toUpperCase();
          }
          String table = StringUtils.formatMessage(tableName, objs);
          String script = StringUtils.formatMessage(ddl, objs);
          candidates.add(new Candidate(client, table, script));
        }
      } else {
        int from = scope.from();
        int to = scope.to();
        for (int i = from; i <= to; i++) {
          String table = StringUtils.formatMessage(tableName, i);
          String script = StringUtils.formatMessage(ddl, i);
          candidates.add(new Candidate(client, table, script));
        }
      }
    } else {
      candidates.add(new Candidate(client, tableName, ddl));
    }
    return candidates;
  }

  private static class Candidate {

    private MysqlClient client;
    private String table;
    private String script;

    private Candidate(MysqlClient client, String table, String script) {
      this.client = client;
      this.table = table;
      this.script = script;
    }
  }
}
