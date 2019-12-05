package com.mongodb.async.client;

import com.mongodb.async.SingleResultCallback;
import org.goblinframework.api.annotation.Compatible;
import org.goblinframework.database.mongo.module.monitor.MNG;
import org.jetbrains.annotations.NotNull;

@Compatible(
    group = "org.mongodb",
    artifact = "mongodb-driver-async",
    version = "3.11.0"
)
@SuppressWarnings("deprecation")
class GoblinSingleResultCallback<T> implements SingleResultCallback<T> {

  private final SingleResultCallback<T> delegator;
  private final MNG mng;

  GoblinSingleResultCallback(@NotNull SingleResultCallback<T> delegator,
                             @NotNull MNG mng) {
    this.delegator = delegator;
    this.mng = mng;
  }

  @Override
  public void onResult(T result, Throwable t) {
    try {
      delegator.onResult(result, t);
    } finally {
      mng.complete();
    }
  }
}
