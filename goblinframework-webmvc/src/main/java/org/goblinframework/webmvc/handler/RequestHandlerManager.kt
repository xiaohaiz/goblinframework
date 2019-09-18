package org.goblinframework.webmvc.handler

import org.goblinframework.core.mbean.GoblinManagedBean
import org.goblinframework.core.mbean.GoblinManagedObject
import org.goblinframework.core.util.StringUtils
import org.goblinframework.webmvc.mapping.controller.ControllerMappingBuilder
import org.goblinframework.webmvc.mapping.method.MethodMappingBuilder
import org.goblinframework.webmvc.servlet.ServletRequest
import org.goblinframework.webmvc.servlet.ServletResponse
import org.goblinframework.webmvc.setting.RequestHandlerSetting
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.max
import kotlin.math.min

@GoblinManagedBean("WEBMVC")
class RequestHandlerManager(private val setting: RequestHandlerSetting)
  : GoblinManagedObject(), RequestHandlerManagerMXBean {

  private val withoutPathVariables = ControllerMappingMap()
  private val withPathVariables = mutableMapOf<String, ControllerMappingMap>()
  private val conditions = PatternsRequestConditionMap()
  private val maxDepth = AtomicInteger()

  init {
    val methodMappings = MethodMappingBuilder.scan(setting.controllerSetting())
    val controllerMappings = ControllerMappingBuilder.build(methodMappings)

    controllerMappings.filterNot { it.urlContainsPathVariable }
        .forEach {
          val id = it.uniqueIdentification
          withoutPathVariables[id] = it
        }

    controllerMappings.filter { it.urlContainsPathVariable }
        .forEach {
          val url = it.requestURL
          var s = StringUtils.substringBefore(url, "{")
          s = StringUtils.substringBeforeLast(s, "/")
          var prefix = "$s/"
          prefix = it.requestMethod.name + " " + prefix
          val depth = StringUtils.countMatches(prefix, "/")
          maxDepth.updateAndGet { operand -> max(operand, depth) }
          withPathVariables.computeIfAbsent(prefix) {
            ControllerMappingMap()
          }[it.requestURL] = it
        }

    withPathVariables.forEach { (prefix, mappings) ->
      val patterns = mappings.keys
      conditions[prefix] = PatternsRequestCondition(patterns)
    }
  }

  fun lookup(request: ServletRequest, response: ServletResponse): RequestHandler? {
    val lookupPath = request.getLookupPath()
    val url = "${request.method!!.name} $lookupPath"
    var mapping = withoutPathVariables[url]
    if (mapping != null) {
      return RequestHandlerImpl(setting, request, response, lookupPath, lookupPath, mapping)
    }

    val depth = StringUtils.countMatches(url, "/")
    val startDepth = min(depth, maxDepth.get())

    val segments = StringUtils.split(url, "/")
    for (i in startDepth downTo 1) {
      val prefix = composePrefix(segments, i)
      val condition = conditions[prefix] ?: continue
      val matching = condition.getMatchingPatterns(lookupPath)
      val bestMatchingPattern = matching.firstOrNull() ?: continue
      mapping = withPathVariables[prefix]!![bestMatchingPattern]!!
      return RequestHandlerImpl(setting, request, response, lookupPath, bestMatchingPattern, mapping)
    }
    return null
  }

  private fun composePrefix(segments: Array<String>, index: Int): String {
    val list = ArrayList<String>(index + 1)
    list.addAll(listOf(*segments).subList(0, index))
    list.add("")
    return StringUtils.join(list, "/")
  }

  override fun getName(): String {
    return setting.name()
  }
}