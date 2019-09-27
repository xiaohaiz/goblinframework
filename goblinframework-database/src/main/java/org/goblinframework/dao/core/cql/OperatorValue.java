package org.goblinframework.dao.core.cql;

import java.util.LinkedHashMap;

/**
 * Data structure for presenting operator and its value.
 * The value could be anything, if the value is {@link Criteria#NO_VALUE} means the operator requires no value.
 * If the value is {@link Criteria#NULL_VALUE} means the operator value is null.
 * This is not thread safe.
 */
public class OperatorValue extends LinkedHashMap<Operator, Object> {

  public boolean hasOperator(Operator operator) {
    return operator != null && containsKey(operator);
  }

  /**
   * Check if has content or not, notice that {@link Operator#$not} must work with other operators.
   *
   * @return true or false
   */
  public boolean hasContent() {
    return !isEmpty() && !(size() == 1 && keySet().iterator().next() == Operator.$not);
  }
}
