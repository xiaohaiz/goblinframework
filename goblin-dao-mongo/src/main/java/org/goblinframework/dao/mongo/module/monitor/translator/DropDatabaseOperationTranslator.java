package org.goblinframework.dao.mongo.module.monitor.translator;

import com.mongodb.operation.DropDatabaseOperation;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

@SuppressWarnings("deprecation")
final public class DropDatabaseOperationTranslator implements OperationTranslator {

  @NotNull private final DropDatabaseOperation operation;

  DropDatabaseOperationTranslator(@NotNull DropDatabaseOperation operation) {
    this.operation = operation;
  }

  @NotNull
  @Override
  public String translate() throws Exception {
    Field field = DropDatabaseOperation.class.getDeclaredField("databaseName");
    field.setAccessible(true);
    String databaseName = (String) field.get(operation);
    return "databaseName=" + databaseName
        + " writeConcern=" + _OperationTranslatorKt.translateWriteConcern(operation.getWriteConcern());
  }
}
