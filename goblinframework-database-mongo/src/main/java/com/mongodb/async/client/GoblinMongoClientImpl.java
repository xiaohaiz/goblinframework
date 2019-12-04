package com.mongodb.async.client;

import com.mongodb.connection.Cluster;
import org.goblinframework.api.annotation.Compatible;
import org.goblinframework.core.system.GoblinSystem;
import org.goblinframework.core.system.RuntimeMode;
import org.goblinframework.core.util.ExceptionUtils;

import java.io.Closeable;
import java.lang.reflect.Field;

@Compatible(
    group = "org.mongodb",
    artifact = "mongodb-driver-async",
    version = "3.11.0"
)
@SuppressWarnings("deprecation")
class GoblinMongoClientImpl extends MongoClientImpl {

  GoblinMongoClientImpl(MongoClientSettings settings, Cluster cluster) {
    super(settings, cluster, (Closeable) null);
    try {
      customizeOperationExecutor();
    } catch (Throwable t) {
      throw ExceptionUtils.propagate(t);
    }
  }

  private void customizeOperationExecutor() throws Throwable {
    Field field = MongoClientImpl.class.getDeclaredField("executor");
    field.setAccessible(true);
    OperationExecutor original = (OperationExecutor) field.get(this);
    GoblinOperationExecutor executor = new GoblinOperationExecutor(original);
    field.set(this, executor);
  }

  @Override
  public MongoDatabase getDatabase(String name) {
    RuntimeMode mode = GoblinSystem.runtimeMode();
    String databaseName = name;
    if (mode == RuntimeMode.UNIT_TEST) {
      // When executing unit tests, force use 'goblin-test-'
      // as default database prefix
      databaseName = "goblin-test-" + databaseName;
    }
    return super.getDatabase(databaseName);
  }
}
