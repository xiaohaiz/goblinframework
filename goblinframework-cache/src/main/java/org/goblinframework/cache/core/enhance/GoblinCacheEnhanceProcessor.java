package org.goblinframework.cache.core.enhance;

import org.goblinframework.api.common.Singleton;
import org.goblinframework.api.container.SpringContainerBeanPostProcessor;

@Singleton
final public class GoblinCacheEnhanceProcessor implements SpringContainerBeanPostProcessor {

  public static final GoblinCacheEnhanceProcessor INSTANCE = new GoblinCacheEnhanceProcessor();

  private GoblinCacheEnhanceProcessor() {
  }
}
