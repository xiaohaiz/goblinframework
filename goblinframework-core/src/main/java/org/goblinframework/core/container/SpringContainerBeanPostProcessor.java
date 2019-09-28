package org.goblinframework.core.container;

import org.goblinframework.api.core.Ordered;
import org.jetbrains.annotations.Nullable;

public interface SpringContainerBeanPostProcessor extends Ordered {

  @Override
  default int getOrder() {
    return 0;
  }

  @Nullable
  default Object postProcessBeforeInitialization(Object bean, String beanName) {
    return bean;
  }

  @Nullable
  default Object postProcessAfterInitialization(Object bean, String beanName) {
    return bean;
  }
}
