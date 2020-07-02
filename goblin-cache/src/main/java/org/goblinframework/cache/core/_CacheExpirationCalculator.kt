package org.goblinframework.cache.core

import org.goblinframework.cache.annotation.CacheExpiration
import org.goblinframework.core.util.DateUtils
import kotlin.math.max

/**
 * Calculate expiration seconds of specified [CacheExpiration] annotation.
 */
fun calculateExpirationInSeconds(annotation: CacheExpiration): Int {
  require(annotation.enable)
  return calculateExpirationInSeconds(annotation.policy, annotation.value)
}

/**
 * Calculate expiration seconds of specified [CacheExpiration.Policy] and value.
 * Return the specified value directly in case of policy is [CacheExpiration.Policy.FIXED]
 */
fun calculateExpirationInSeconds(policy: CacheExpiration.Policy, value: Int): Int {
  return when (policy) {
    CacheExpiration.Policy.FIXED -> max(0, value)
    CacheExpiration.Policy.TODAY -> DateUtils.getCurrentToDayEndSecond()
    CacheExpiration.Policy.THIS_WEEK -> DateUtils.getCurrentToWeekEndSecond()
    CacheExpiration.Policy.THIS_MONTH -> DateUtils.getCurrentToMonthEndSecond()
  }
}