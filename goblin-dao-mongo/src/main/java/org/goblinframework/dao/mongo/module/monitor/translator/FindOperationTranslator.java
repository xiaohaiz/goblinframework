package org.goblinframework.dao.mongo.module.monitor.translator;

import com.mongodb.operation.FindOperation;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

@SuppressWarnings("deprecation")
final public class FindOperationTranslator implements OperationTranslator {

  @NotNull private final FindOperation<?> operation;

  FindOperationTranslator(@NotNull FindOperation<?> operation) {
    this.operation = operation;
  }

  @NotNull
  @Override
  public String translate() {
    return "namespace=" + operation.getNamespace()
        + " decoder=" + operation.getDecoder()
        + " retryReads=" + operation.getRetryReads()
        + " filter=" + operation.getFilter()
        + " batchSize=" + operation.getBatchSize()
        + " limit=" + operation.getLimit()
        + " modifiers=" + operation.getModifiers()
        + " projection=" + operation.getProjection()
        + " maxTimeMS=" + operation.getMaxTime(TimeUnit.MILLISECONDS)
        + " maxAwaitTimeMS=" + operation.getMaxAwaitTime(TimeUnit.MILLISECONDS)
        + " skip=" + operation.getSkip()
        + " sort=" + operation.getSort()
        + " cursorType=" + operation.getCursorType()
        + " slaveOk=" + operation.isSlaveOk()
        + " oplogReplay=" + operation.isOplogReplay()
        + " noCursorTimeout=" + operation.isNoCursorTimeout()
        + " partial=" + operation.isPartial()
        + " collation=" + operation.getCollation()
        + " comment=" + operation.getComment()
        + " hint=" + operation.getHint()
        + " max=" + operation.getMax()
        + " min=" + operation.getMin()
        + " maxScan=" + operation.getMaxScan()
        + " returnKey=" + operation.isReturnKey()
        + " showRecordId=" + operation.isShowRecordId()
        + " snapshot=" + operation.isSnapshot();
  }
}
