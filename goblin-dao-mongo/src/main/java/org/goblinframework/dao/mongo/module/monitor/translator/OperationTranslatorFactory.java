package org.goblinframework.dao.mongo.module.monitor.translator;

import com.mongodb.operation.AsyncReadOperation;
import com.mongodb.operation.AsyncWriteOperation;
import com.mongodb.operation.DropDatabaseOperation;
import com.mongodb.operation.ListDatabasesOperation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
final public class OperationTranslatorFactory {

  @Nullable
  public static OperationTranslator createTranslator(@NotNull AsyncReadOperation<?> readOperation) {
    if (readOperation instanceof ListDatabasesOperation) {
      return new ListDatabasesOperationTranslator((ListDatabasesOperation<?>) readOperation);
    }
    return null;
  }

  @Nullable
  public static OperationTranslator createTranslator(@NotNull AsyncWriteOperation<?> writeOperation) {
    if (writeOperation instanceof DropDatabaseOperation) {
      return new DropDatabaseOperationTranslator((DropDatabaseOperation) writeOperation);
    }
    return null;
  }
}
