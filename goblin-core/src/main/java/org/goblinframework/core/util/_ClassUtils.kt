package org.goblinframework.core.util

/**
 * Return the default ClassLoader, typically try to use the thread context ClassLoader first.
 */
fun getDefaultClassLoader(): ClassLoader {
  val classLoader = org.springframework.util.ClassUtils.getDefaultClassLoader()
  requireNotNull(classLoader)
  return classLoader
}

/**
 * Load the class represented by specified className using the default ClassLoader.
 */
@Throws(ClassNotFoundException::class)
fun loadClass(className: String, initialize: Boolean = true): Class<*> {
  return org.apache.commons.lang3.ClassUtils.getClass(getDefaultClassLoader(), className, initialize)
}

