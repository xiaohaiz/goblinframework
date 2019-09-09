package org.goblinframework.webmvc.mapping.method

import org.apache.commons.lang3.tuple.ImmutablePair
import org.goblinframework.core.container.ContainerManagedBean
import org.goblinframework.core.util.ClassUtils
import org.goblinframework.webmvc.mapping.MalformedMappingException
import org.goblinframework.webmvc.setting.ControllerSetting
import org.springframework.beans.factory.BeanFactoryUtils
import org.springframework.web.bind.annotation.RequestMapping
import java.lang.reflect.Modifier
import java.util.*

object MethodMappingBuilder {

  fun scan(setting: ControllerSetting): List<MethodMapping> {
    val types = IdentityHashMap<Class<*>, Long>()
    val mappings = mutableListOf<MethodMapping>()
    setting.applicationContext()?.run {
      BeanFactoryUtils.beanNamesForTypeIncludingAncestors(this, Any::class.java, true, false)
          .map { ImmutablePair.of(it, getType(it)) }
          .filter { it.right != null }
          .map { ImmutablePair.of(it.left, ClassUtils.filterCglibProxyClass(it.right!!)) }
          .filter { it.right.isAnnotationPresent(RequestMapping::class.java) }
          .forEach { p ->
            val name = p.left
            val type = p.right
            types.putIfAbsent(type, System.currentTimeMillis())?.run { throw MalformedMappingException() }
            type.declaredMethods.asList()
                .filterNot { Modifier.isStatic(it.modifiers) }
                .filterNot { it.isBridge }
                .filter { it.isAnnotationPresent(RequestMapping::class.java) }
                .map { ManagedMethodMapping(type, it, ContainerManagedBean(name, this)) }
                .forEach { mappings.add(it) }
          }

    }
    setting.controllers()
        .map { ImmutablePair.of(it, ClassUtils.filterCglibProxyClass(it.javaClass)) }
        .filter { it.right.isAnnotationPresent(RequestMapping::class.java) }
        .forEach { p ->
          val bean = p.left
          val type = p.right
          types.putIfAbsent(type, System.currentTimeMillis())?.run { throw MalformedMappingException() }
          type.declaredMethods.asList()
              .filterNot { Modifier.isStatic(it.modifiers) }
              .filterNot { it.isBridge }
              .filter { it.isAnnotationPresent(RequestMapping::class.java) }
              .map { StaticMethodMapping(type, it, bean) }
              .forEach { mappings.add(it) }
        }
    return Collections.unmodifiableList(mappings)
  }
}