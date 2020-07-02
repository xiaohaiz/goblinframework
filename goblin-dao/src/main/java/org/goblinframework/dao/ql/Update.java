package org.goblinframework.dao.ql;

import org.goblinframework.core.util.ArrayUtils;
import org.goblinframework.core.util.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class Update {

  private final Map<Operator, FieldValue> localChain = Collections.synchronizedMap(new LinkedHashMap<>());

  public Map<Operator, FieldValue> export() {
    return new LinkedHashMap<>(localChain);
  }

  public static Update update(String field, Object value) {
    return new Update().set(field, value);
  }

  /**
   * Syntax: <code>{ $inc: { &lt;field1&gt;: &lt;amount1&gt;, &lt;field2&gt;: &lt;amount2&gt;, ... } }</code>
   *
   * @param field the field
   * @param value the number to inc
   * @return current update
   */
  public Update inc(String field, Number value) {
    add(Operator.$inc, field, value);
    return this;
  }

  public Update mul(String field, Number multiplier) {
    add(Operator.$mul, field, multiplier);
    return this;
  }

  public Update setOnInsert(String field, Object value) {
    add(Operator.$setOnInsert, field, value);
    return this;
  }

  public Update set(String field, Object value) {
    add(Operator.$set, field, value);
    return this;
  }

  public Update unset(String field) {
    add(Operator.$unset, field, "");
    return this;
  }

  public Update min(String field, Object value) {
    add(Operator.$min, field, value);
    return this;
  }

  public Update max(String field, Object value) {
    add(Operator.$max, field, value);
    return this;
  }

  public Update currentDate(String field) {
    add(Operator.$currentDate, field, true);
    return this;
  }

  public Update addToSet(String field, Object[] values) {
    add(Operator.$addToSet, field, Arrays.copyOf(values, values.length));
    return this;
  }

  public Update pop(String field, Position position) {
    add(Operator.$pop, field, position);
    return this;
  }

  public Update pullAll(String field, Object[] values) {
    if (StringUtils.isBlank(field)) {
      throw new IllegalArgumentException();
    }
    if (ArrayUtils.isEmpty(values)) {
      throw new IllegalArgumentException();
    }
    add(Operator.$pullAll, field, Arrays.copyOf(values, values.length));
    return this;
  }

  public Update pull(Criteria criteria) {
    if (criteria == null) {
      throw new IllegalArgumentException();
    }
    if (localChain.containsKey(Operator.$pull)) {
      throw new IllegalArgumentException();
    }
    add(Operator.$pull, "N/A", criteria);
    return this;
  }

  public Update push(String field, Object[] values) {
    if (StringUtils.isBlank(field)) {
      throw new IllegalArgumentException();
    }
    if (ArrayUtils.isEmpty(values)) {
      throw new IllegalArgumentException();
    }
    add(Operator.$push, field, Arrays.copyOf(values, values.length));
    return this;
  }

  private void add(Operator operator, String field, Object value) {
    if (!localChain.containsKey(operator)) {
      localChain.put(operator, new FieldValue());
    }
    FieldValue fieldValue = localChain.get(operator);
    fieldValue.put(field, value);
  }
}
