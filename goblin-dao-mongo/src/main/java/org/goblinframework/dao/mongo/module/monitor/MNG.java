package org.goblinframework.dao.mongo.module.monitor;

import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.operation.AsyncReadOperation;
import com.mongodb.operation.AsyncWriteOperation;
import org.goblinframework.core.monitor.AbstractInstruction;
import org.goblinframework.core.monitor.InstructionTranslator;
import org.goblinframework.core.util.StringUtils;
import org.goblinframework.dao.mongo.module.monitor.translator.OperationTranslator;
import org.goblinframework.dao.mongo.module.monitor.translator.OperationTranslatorFactory;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("deprecation")
final public class MNG extends AbstractInstruction {

  public MNG() {
    super(Id.MNG, Mode.ASY, true);
  }

  public AsyncReadOperation<?> readOperation;
  public AsyncWriteOperation<?> writeOperation;
  public ReadPreference readPreference;
  public ReadConcern readConcern;

  @NotNull
  @Override
  public InstructionTranslator translator() {
    return pretty -> {
      if (!pretty) {
        return asShortText();
      }
      String operation = null;
      if (readOperation != null) {
        operation = readOperation.getClass().getSimpleName();
      }
      if (operation == null) {
        operation = writeOperation.getClass().getSimpleName();
      }

      OperationTranslator translator;
      if (readOperation != null) {
        translator = OperationTranslatorFactory.createTranslator(readOperation);
      } else {
        translator = OperationTranslatorFactory.createTranslator(writeOperation);
      }
      String operationDetail = null;
      if (translator != null) {
        try {
          operationDetail = translator.translate();
        } catch (Exception ignore) {
        }
      }

      if (StringUtils.isBlank(operationDetail)) {
        return String.format("%s %s",
            asLongText(),
            operation);
      } else {
        return String.format("%s %s %s",
            asLongText(),
            operation,
            operationDetail);
      }
    };
  }

}
