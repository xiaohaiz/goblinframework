package org.goblinframework.dao.mongo.module.monitor.translator;

import org.jetbrains.annotations.NotNull;

public interface OperationTranslator {

  @NotNull
  String translate() throws Exception;

}
