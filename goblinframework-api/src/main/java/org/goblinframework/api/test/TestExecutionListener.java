package org.goblinframework.api.test;

import org.goblinframework.api.common.Ordered;

public interface TestExecutionListener extends Ordered {

  void prepareTestInstance(TestContext testContext) throws Exception;

  void beforeTestExecution(TestContext testContext) throws Exception;

  void beforeTestClass(TestContext testContext) throws Exception;

  void beforeTestMethod(TestContext testContext) throws Exception;

  void afterTestExecution(TestContext testContext) throws Exception;

  void afterTestClass(TestContext testContext) throws Exception;

  void afterTestMethod(TestContext testContext) throws Exception;

}
