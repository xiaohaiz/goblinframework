package org.goblinframework.core.container;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.context.support.ApplicationObjectSupport;

abstract public class SpringManagedBean extends ApplicationObjectSupport
    implements BeanClassLoaderAware, BeanNameAware, BeanFactoryAware, InitializingBean, DisposableBean {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  protected String beanName;
  protected ClassLoader beanClassLoader;
  protected BeanFactory beanFactory;

  @Override
  public void setBeanClassLoader(ClassLoader classLoader) {
    this.beanClassLoader = classLoader;
  }

  @Override
  public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
    this.beanFactory = beanFactory;
  }

  @Override
  public void setBeanName(String name) {
    this.beanName = name;
  }

  @Override
  public void destroy() throws Exception {
  }

  @Override
  public void afterPropertiesSet() throws Exception {
  }
}
