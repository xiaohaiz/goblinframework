package org.goblinframework.dao.core.cql;

import org.bson.types.ObjectId;
import org.goblinframework.core.conversion.ConversionService;

import javax.management.ObjectName;
import java.io.File;
import java.time.Instant;
import java.util.*;

abstract public class EntityFieldValueTranslator {

  public static Object translate(Object value) {
    if (value == null || value == Criteria.NULL_VALUE) {
      return null;
    }
    if (value.getClass().isEnum()) {
      return ((Enum) value).name();
    }
    if (value instanceof Calendar
        || value instanceof Instant) {
      return ConversionService.INSTANCE.convert(value, Date.class);
    }
    if (value instanceof File) {
      return ((File) value).getPath();
    }
    if (value instanceof ObjectId) {
      return ((ObjectId) value).toHexString();
    }
    if (value instanceof ObjectName) {
      return value.toString();
    }

    if (value instanceof Collection) {
      Collection collection = (Collection) value;
      List<Object> list = new LinkedList<>();
      for (Object element : collection) {
        list.add(translate(element));
      }
      return list;
    }
    return value;
  }
}
