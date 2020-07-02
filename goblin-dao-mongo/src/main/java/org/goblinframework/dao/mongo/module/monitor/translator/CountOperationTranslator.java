package org.goblinframework.dao.mongo.module.monitor.translator;

import com.mongodb.MongoNamespace;
import com.mongodb.internal.client.model.CountStrategy;
import com.mongodb.operation.CountOperation;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("deprecation")
final public class CountOperationTranslator implements OperationTranslator {

  @NotNull private final CountOperation operation;

  CountOperationTranslator(@NotNull CountOperation operation) {
    this.operation = operation;
  }

  @NotNull
  @Override
  public String translate() throws Exception {
    Field field = CountOperation.class.getDeclaredField("namespace");
    field.setAccessible(true);
    MongoNamespace namespace = (MongoNamespace) field.get(operation);

    field = CountOperation.class.getDeclaredField("countStrategy");
    field.setAccessible(true);
    CountStrategy countStrategy = (CountStrategy) field.get(operation);
    return "namespace=" + namespace
        + " countStrategy=" + countStrategy
        + " retryReads=" + operation.getRetryReads()
        + " filter=" + operation.getFilter()
        + " hint=" + operation.getHint()
        + " skip=" + operation.getSkip()
        + " limit=" + operation.getLimit()
        + " maxTimeMS=" + operation.getMaxTime(TimeUnit.MILLISECONDS)
        + " collation=" + operation.getCollation();
  }
}
