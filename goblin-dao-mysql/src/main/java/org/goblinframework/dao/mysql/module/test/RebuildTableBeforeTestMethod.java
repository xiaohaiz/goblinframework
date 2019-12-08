package org.goblinframework.dao.mysql.module.test;

import org.goblinframework.api.annotation.Singleton;
import org.goblinframework.api.test.TestContext;
import org.goblinframework.api.test.TestExecutionListener;
import org.goblinframework.core.util.StringUtils;
import org.goblinframework.dao.mysql.exception.GoblinMysqlConfigException;
import org.goblinframework.database.mysql.client.MysqlClient;
import org.goblinframework.database.mysql.client.MysqlClientManager;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.*;

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
    if (!annotations.isEmpty()) {
      Map<MysqlClient, Set<String>> candidates = new IdentityHashMap<>();
      annotations.forEach(e -> generateTables(e, candidates));
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

  private void generateTables(RebuildTable annotation, Map<MysqlClient, Set<String>> candidates) {
    String connection = annotation.connection();
    MysqlClientManager clientManager = MysqlClientManager.INSTANCE;
    MysqlClient client = clientManager.getMysqlClient(connection);
    if (client == null) {
      throw new GoblinMysqlConfigException("MYSQL connection not found: " + connection);
    }
    List<String> tableList = new ArrayList<>();
    String table = annotation.table();
    RebuildTableScope scope = annotation.scope();
    if (scope.enable()) {
      RebuildTableParameter[] parameters = scope.parameters();
      if (parameters.length > 0) {
        for (RebuildTableParameter parameter : parameters) {
          Object[] objs = new Object[parameter.parameters().length];
          for (int i = 0; i < parameter.parameters().length; i++) {
            objs[i] = parameter.parameters()[i];
          }
          tableList.add(StringUtils.formatMessage(table, objs));
        }
      } else {
        int from = scope.from();
        int to = scope.to();
        for (int i = from; i <= to; i++) {
          tableList.add(StringUtils.formatMessage(table, i));
        }
      }
    } else {
      tableList.add(table);
    }
    candidates.computeIfAbsent(client, _p -> new LinkedHashSet<>()).addAll(tableList);
  }
}
