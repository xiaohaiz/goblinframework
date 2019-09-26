package org.goblinframework.api.test;

import org.goblinframework.api.core.Ordered;
import org.jetbrains.annotations.NotNull;

public interface TestExecutionListener extends Ordered {

  @Override
  default int getOrder() {
    return 0;
  }

  default void prepareTestInstance(@NotNull TestContext testContext) {
  }

  default void beforeTestExecution(@NotNull TestContext testContext) {
  }

  default void beforeTestClass(@NotNull TestContext testContext) {
  }

  default void beforeTestMethod(@NotNull TestContext testContext) {
  }

  default void afterTestExecution(@NotNull TestContext testContext) {
  }

  default void afterTestClass(@NotNull TestContext testContext) {
  }

  default void afterTestMethod(@NotNull TestContext testContext) {
  }

}
