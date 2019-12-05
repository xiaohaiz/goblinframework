package org.goblinframework.webmvc.view

import org.goblinframework.core.container.ContainerManagedBean
import org.goblinframework.webmvc.setting.ViewResolverSetting
import org.springframework.beans.factory.BeanFactoryUtils
import org.springframework.core.OrderComparator

class ViewResolverManager(setting: ViewResolverSetting) {

  private val staticResolvers = mutableListOf<ViewResolver>()
  private val managedResolvers = mutableListOf<ContainerManagedBean>()

  init {
    staticResolvers.addAll(setting.viewResolvers())
    setting.applicationContext()?.run {
      BeanFactoryUtils.beanNamesForTypeIncludingAncestors(this, ViewResolver::class.java, true, false)
          .map { ContainerManagedBean(it, this) }
          .forEach { managedResolvers.add(it) }
    }
  }

  fun lookupView(viewName: String): View? {
    val list = ViewResolverList()
    list.addAll(staticResolvers)
    managedResolvers.map { it.getBean() as ViewResolver }.forEach { list.add(it) }
    for (resolver in list.sortedWith(OrderComparator.INSTANCE)) {
      val view = try {
        resolver.resolve(viewName)
      } catch (ex: Exception) {
        null
      }
      if (view != null) {
        return view
      }
    }
    return null
  }
}