package org.goblinframework.dao.core.cql;

import java.util.LinkedHashMap;

/**
 * Data structure for presenting field and its value.
 */
public class FieldValue extends LinkedHashMap<String, Object> {

  public boolean hasField(String field) {
    return field != null && containsKey(field);
  }

}
