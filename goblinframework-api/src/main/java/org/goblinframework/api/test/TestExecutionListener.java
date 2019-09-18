package org.goblinframework.api.test;

import org.goblinframework.api.common.Ordered;

public interface TestExecutionListener extends Ordered {

  @Override
  default int getOrder() {
    return 0;
  }

  default void prepareTestInstance(TestContext testContext) throws Exception {
  }

  default void beforeTestExecution(TestContext testContext) throws Exception {
  }

  default void beforeTestClass(TestContext testContext) throws Exception {
  }

  default void beforeTestMethod(TestContext testContext) throws Exception {
  }

  default void afterTestExecution(TestContext testContext) throws Exception {
  }

  default void afterTestClass(TestContext testContext) throws Exception {
  }

  default void afterTestMethod(TestContext testContext) throws Exception {
  }

}
