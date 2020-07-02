package org.goblinframework.dao.ql;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.bson.BsonType;

import java.util.*;
import java.util.regex.Pattern;

/**
 * The core criteria data structure. This is not thread safe.
 */
public class Criteria {

  // If the 'value' set to 'NO_VALUE', means current criteria has no value required
  public static final Object NO_VALUE = new Object();
  // If the 'value' set to 'NULL_VALUE', means current criteria has null value
  public static final Object NULL_VALUE = new Object();

  private String currentField;
  private final Map<String, OperatorValue> localChain = Collections.synchronizedMap(new LinkedHashMap<>());

  public Map<String, OperatorValue> export() {
    return new LinkedHashMap<>(localChain);
  }


  /**
   * The joiner could be: {@link Operator#$and}, {@link Operator#$or} and {@link Operator#$nor}.
   */
  private Operator joiner;
  /**
   * Take effects in case of joiner is not null.
   */
  private final List<Criteria> criteriaChain = Collections.synchronizedList(new LinkedList<>());

  public Operator getJoiner() {
    return joiner;
  }

  public List<Criteria> getCriteriaChain() {
    return criteriaChain;
  }

  public static Criteria and(Collection<Criteria> collection) {
    Objects.requireNonNull(collection);
    return and(collection.toArray(new Criteria[collection.size()]));
  }

  /**
   * Join multiple criteria with joiner {@link Operator#$and}.
   *
   * @param criteria criteria array to be joined
   * @return criteria object
   */
  public static Criteria and(Criteria... criteria) {
    Criteria result = new Criteria();
    result.joiner = Operator.$and;
    result.getCriteriaChain().addAll(Arrays.asList(Objects.requireNonNull(criteria)));
    return result;
  }

  public static Criteria or(Collection<Criteria> collection) {
    Objects.requireNonNull(collection);
    return or(collection.toArray(new Criteria[collection.size()]));
  }

  /**
   * Join multiple criteria with joiner {@link Operator#$or}.
   *
   * @param criteria criteria array to be joined
   * @return criteria object
   */
  public static Criteria or(Criteria... criteria) {
    Criteria result = new Criteria();
    result.joiner = Operator.$or;
    result.getCriteriaChain().addAll(Arrays.asList(Objects.requireNonNull(criteria)));
    return result;
  }

  /**
   * Join multiple criteria with joiner {@link Operator#$nor}.
   *
   * @param criteria criteria array to be joined
   * @return criteria object
   */
  public static Criteria nor(Criteria... criteria) {
    Criteria result = new Criteria();
    result.joiner = Operator.$nor;
    result.getCriteriaChain().addAll(Arrays.asList(Objects.requireNonNull(criteria)));
    return result;
  }

  /**
   * Static factory method for rapidly criteria creation starts from specified field.
   *
   * @param field the start field
   * @return new criteria object
   */
  public static Criteria where(String field) {
    return new Criteria().and(field);
  }

  /**
   * Start a new field within current criteria. Notice that must finish the previous field before.
   * Only the and method can move to next (new) field.
   *
   * @param field the new field, duplicate is not allowed
   * @return the criteria
   * @throws IllegalStateException in case of field already set or current operator value is illegal
   */
  public Criteria and(String field) {
    Objects.requireNonNull(field);
    if (localChain.containsKey(field)) {
      currentField = field;
      return this;
    }
    // don't use loadCurrent() here for the current may be null now
    OperatorValue current = localChain.get(currentField);
    if (current != null && !current.hasContent()) {
      String message = String.format("No operator found of field '%s'", currentField);
      throw new IllegalStateException(message);
    }
    currentField = field;
    localChain.put(currentField, new OperatorValue());
    return this;
  }

  /**
   * Syntax: <code>{ &lt;field&gt;: { $eq: &lt;value&gt; } }</code>
   * equivalent to <code>{ field: &lt;value&gt; }</code>
   *
   * @param value the value
   * @return current criteria
   */
  public Criteria is(Object value) {
    OperatorValue current = loadCurrentWithoutOperator(Operator.$eq);
    current.put(Operator.$eq, value == null ? NULL_VALUE : value);
    return this;
  }

  /**
   * Syntax: <code>{field: {$gt: value} }</code>
   *
   * @param value the value
   * @return current criteria
   */
  public Criteria gt(Object value) {
    OperatorValue current = loadCurrentWithoutOperator(Operator.$gt);
    current.put(Operator.$gt, value == null ? NULL_VALUE : value);
    return this;
  }

  /**
   * Syntax: <code>{field: {$gte: value} }</code>
   *
   * @param value the value
   * @return current criteria
   */
  public Criteria gte(Object value) {
    OperatorValue current = loadCurrentWithoutOperator(Operator.$gte);
    current.put(Operator.$gte, value == null ? NULL_VALUE : value);
    return this;
  }

  /**
   * Syntax: <code>{field: {$lt: value} }</code>
   *
   * @param value the value
   * @return current criteria
   */
  public Criteria lt(Object value) {
    OperatorValue current = loadCurrentWithoutOperator(Operator.$lt);
    current.put(Operator.$lt, value == null ? NULL_VALUE : value);
    return this;
  }

  /**
   * Syntax: <code>{ field: { $lte: value} }</code>
   *
   * @param value the value
   * @return current criteria
   */
  public Criteria lte(Object value) {
    OperatorValue current = loadCurrentWithoutOperator(Operator.$lte);
    current.put(Operator.$lte, value == null ? NULL_VALUE : value);
    return this;
  }

  /**
   * Syntax: <code>{field: {$ne: value} }</code>
   *
   * @param value the value
   * @return current criteria
   */
  public Criteria ne(Object value) {
    OperatorValue current = loadCurrentWithoutOperator(Operator.$ne);
    current.put(Operator.$ne, value == null ? NULL_VALUE : value);
    return this;
  }

  /**
   * Syntax: <code>{ field: { $in: [&lt;value1&gt;, &lt;value2&gt;, ... &lt;valueN&gt; ] } }</code>
   *
   * @param value the value must be non empty {@link Collection}
   * @return current criteria
   */
  @SuppressWarnings("unchecked")
  public Criteria in(Collection value) {
    if (value == null || value.isEmpty()) {
      throw new IllegalArgumentException("Empty collection is not allowed");
    }
    OperatorValue current = loadCurrentWithoutOperator(Operator.$in);
    current.put(Operator.$in, new LinkedList(value));
    return this;
  }

  /**
   * Syntax: <code>{ field: { $nin: [&lt;value1&gt;, &lt;value2&gt;, ... &lt;valueN&gt; ] } }</code>
   *
   * @param value the value must be non empty {@link Collection}
   * @return current criteria
   */
  @SuppressWarnings("unchecked")
  public Criteria nin(Collection value) {
    if (value == null || value.isEmpty()) {
      throw new IllegalArgumentException("Empty collection is not allowed");
    }
    OperatorValue current = loadCurrentWithoutOperator(Operator.$nin);
    current.put(Operator.$nin, new LinkedList(value));
    return this;
  }

  /**
   * Syntax: <code>{ field: { $not: { &lt;operator-expression&gt; } } }</code>
   * The $not operator must work with other operators.
   *
   * @return current criteria
   */
  public Criteria not() {
    OperatorValue current = loadCurrentWithoutOperator(Operator.$not);
    current.put(Operator.$not, NO_VALUE);
    return this;
  }

  /**
   * Syntax: <code>{ field: { $exists: &lt;boolean&gt; } }</code>
   *
   * @param value the boolean value
   * @return current criteria
   */
  public Criteria exists(boolean value) {
    OperatorValue current = loadCurrentWithoutOperator(Operator.$exists);
    current.put(Operator.$exists, value);
    return this;
  }

  public Criteria exists() {
    return exists(true);
  }

  public Criteria notExists() {
    return exists(false);
  }

  /**
   * Syntax: <code>{ field: { $type: &lt;BSON type number&gt; | &lt;String alias&gt; } }</code>
   *
   * @param value bson type, null is not acceptable
   * @return current criteria
   */
  public Criteria type(BsonType value) {
    Objects.requireNonNull(value);
    OperatorValue current = loadCurrentWithoutOperator(Operator.$type);
    current.put(Operator.$type, value);
    return this;
  }

  /**
   * Syntax: <code>{ field: { $mod: [ divisor, remainder ] } }</code>
   *
   * @param divisor   divisor
   * @param remainder remainder
   * @return current criteria
   */
  public Criteria mod(int divisor, int remainder) {
    if (divisor == 0) {
      throw new IllegalArgumentException("The divisor must not be zero");
    }
    OperatorValue current = loadCurrentWithoutOperator(Operator.$mod);
    current.put(Operator.$mod, new ImmutablePair<>(divisor, remainder));
    return this;
  }

  /**
   * Syntax: <code>{ &lt;field&gt;: { $regex: /pattern/, $options: '&lt;options&gt;' } }</code>
   *
   * @param value the {@link Pattern}, must not be null
   * @return current criteria
   */
  public Criteria regex(Pattern value) {
    Objects.requireNonNull(value);
    OperatorValue current = loadCurrentWithoutOperator(Operator.$regex);
    current.put(Operator.$regex, value);
    return this;
  }

  /**
   * Syntax: <code>{ &lt;field&gt;: { $all: [ &lt;value1&gt; , &lt;value2&gt; ... ] } }</code>
   *
   * @param value the value, empty is not allowed
   * @return current criteria
   */
  @SuppressWarnings("unchecked")
  public Criteria all(Collection value) {
    if (value == null || value.isEmpty()) {
      throw new IllegalArgumentException("Empty collection is not allowed");
    }
    OperatorValue current = loadCurrentWithoutOperator(Operator.$all);
    current.put(Operator.$all, new LinkedList(value));
    return this;
  }

  public Criteria elemMatch(Criteria criteria) {
    Objects.requireNonNull(criteria, "$elemMatch 'criteria' required");
    OperatorValue current = loadCurrentWithoutOperator(Operator.$elemMatch);
    current.put(Operator.$elemMatch, criteria);
    return this;
  }

  /**
   * Syntax: <code>{ field: { $size: value } }</code>
   *
   * @param value the size value
   * @return current criteria
   */
  public Criteria size(int value) {
    if (value < 0) {
      throw new IllegalArgumentException("Illegal size");
    }
    OperatorValue current = loadCurrentWithoutOperator(Operator.$size);
    current.put(Operator.$size, value);
    return this;
  }

  public Criteria like(String value) {
    if (value == null) {
      throw new IllegalArgumentException();
    }
    OperatorValue current = loadCurrentWithoutOperator(Operator.$like);
    current.put(Operator.$like, value);
    return this;
  }

  /**
   * Load and ensure the current {@link OperatorValue} exists.
   *
   * @return current operator value
   * @throws IllegalStateException in case of no current operator value (no field set)
   */
  private OperatorValue loadCurrent() {
    OperatorValue operatorValue = currentField == null ? null : localChain.get(currentField);
    if (operatorValue == null) {
      String message = "Set 'field' is required";
      throw new IllegalStateException(message);
    }
    return operatorValue;
  }

  /**
   * Load and ensure current {@link OperatorValue} contains no specified {@link Operator}
   *
   * @param operator the operator must not be contained in current
   * @return current operator value
   * @throws IllegalStateException in case of current operator contains specified operator
   */
  private OperatorValue loadCurrentWithoutOperator(Operator operator) {
    OperatorValue current = loadCurrent();
    if (current.hasOperator(operator)) {
      String message = String.format("Operator '%s' already set of field '%s'", operator, currentField);
      throw new IllegalStateException(message);
    }
    return current;
  }

}
