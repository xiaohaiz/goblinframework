package org.goblinframework.webmvc.handler

import org.goblinframework.webmvc.util.DefaultPathMatcher

class PatternsRequestCondition(patterns: Set<String>) {

  private val pathMatcher = DefaultPathMatcher.INSTANCE
  private val patterns = prependLeadingSlash(patterns)

  private fun prependLeadingSlash(patterns: Set<String>): Set<String> {
    return patterns
        .map { it.trim() }
        .map { if (it.startsWith("/")) it else "/$it" }
        .toSet()
  }

  fun getMatchingPatterns(lookupPath: String): List<String> {
    return patterns.mapNotNull { getMatchingPattern(it, lookupPath) }
        .sortedWith(pathMatcher.getPatternComparator(lookupPath))
        .toList()
  }

  private fun getMatchingPattern(pattern: String, lookupPath: String): String? {
    if (pattern == lookupPath) {
      return pattern
    }
    val hasSuffix = pattern.indexOf('.') != -1
    if (!hasSuffix && this.pathMatcher.match("$pattern.*", lookupPath)) {
      return "$pattern.*"
    }
    if (this.pathMatcher.match(pattern, lookupPath)) {
      return pattern
    }
    return if (!pattern.endsWith("/") && this.pathMatcher.match("$pattern/", lookupPath)) {
      "$pattern/"
    } else null
  }
}