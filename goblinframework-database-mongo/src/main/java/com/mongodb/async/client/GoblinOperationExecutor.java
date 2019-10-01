package com.mongodb.async.client;

import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.operation.AsyncReadOperation;
import com.mongodb.operation.AsyncWriteOperation;
import org.goblinframework.api.annotation.Compatible;
import org.goblinframework.database.mongo.module.monitor.MNG;
import org.jetbrains.annotations.NotNull;

@Compatible(
    group = "org.mongodb",
    artifact = "mongodb-driver-async",
    version = "3.11.0"
)
@SuppressWarnings("deprecation")
class GoblinOperationExecutor implements OperationExecutor {

  private final OperationExecutor delegator;

  GoblinOperationExecutor(@NotNull OperationExecutor delegator) {
    this.delegator = delegator;
  }

  @Override
  public <T> void execute(AsyncReadOperation<T> operation,
                          ReadPreference readPreference,
                          ReadConcern readConcern,
                          SingleResultCallback<T> callback) {
    try (MNG mng = new MNG()) {
      GoblinSingleResultCallback<T> gc = new GoblinSingleResultCallback<>(callback, mng);
      delegator.execute(operation, readPreference, readConcern, gc);
    }
  }

  @Override
  public <T> void execute(AsyncReadOperation<T> operation, ReadPreference readPreference, ReadConcern readConcern, ClientSession session, SingleResultCallback<T> callback) {
    try (MNG mng = new MNG()) {
      GoblinSingleResultCallback<T> gc = new GoblinSingleResultCallback<>(callback, mng);
      delegator.execute(operation, readPreference, readConcern, session, gc);
    }
  }

  @Override
  public <T> void execute(AsyncWriteOperation<T> operation, ReadConcern readConcern, SingleResultCallback<T> callback) {
    try (MNG mng = new MNG()) {
      GoblinSingleResultCallback<T> gc = new GoblinSingleResultCallback<>(callback, mng);
      delegator.execute(operation, readConcern, gc);
    }
  }

  @Override
  public <T> void execute(AsyncWriteOperation<T> operation, ReadConcern readConcern, ClientSession session, SingleResultCallback<T> callback) {
    try (MNG mng = new MNG()) {
      GoblinSingleResultCallback<T> gc = new GoblinSingleResultCallback<>(callback, mng);
      delegator.execute(operation, readConcern, session, gc);
    }
  }
}
