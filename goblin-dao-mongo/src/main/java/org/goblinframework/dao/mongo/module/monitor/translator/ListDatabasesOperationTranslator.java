package org.goblinframework.dao.mongo.module.monitor.translator;

import com.mongodb.operation.ListDatabasesOperation;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

@SuppressWarnings("deprecation")
final public class ListDatabasesOperationTranslator implements OperationTranslator {

  @NotNull private final ListDatabasesOperation<?> operation;

  ListDatabasesOperationTranslator(@NotNull ListDatabasesOperation<?> operation) {
    this.operation = operation;
  }

  @NotNull
  @Override
  public String translate() {
    return "retryReads=" +
        operation.getRetryReads() +
        " " +
        "maxTimeMS=" +
        operation.getMaxTime(TimeUnit.MILLISECONDS) +
        " " +
        "filter=" +
        operation.getFilter() +
        " " +
        "nameOnly=" +
        operation.getNameOnly();
  }
}
