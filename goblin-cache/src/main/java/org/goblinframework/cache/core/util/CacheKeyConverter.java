package org.goblinframework.cache.core.util;

import org.goblinframework.core.util.PrimitiveUtils;
import org.goblinframework.core.util.StringUtils;

import java.lang.reflect.Array;
import java.util.*;

abstract public class CacheKeyConverter {

  public static String objectToCacheKey(Object object, String defaultValue) {
    if (object == null) {
      return defaultValue;
    }
    return objectToCacheKey(object);
  }

  // For text memcached protocol, ' ', '\r', '\n' and 0 aren't supported.
  // For memcached/couchbase, the max key size is 250

  public static String objectToCacheKey(Object object) {
    if (object == null) {
      return "null";
    }
    if (object.getClass().isPrimitive()) {
      if (object.getClass() == char.class) {
        if ((char) object == ' ') {
          return "#32";
        } else if ((char) object == '\r') {
          return "#13";
        } else if ((char) object == '\n') {
          return "#10";
        } else if ((char) object == (char) 0) {
          return "#00";
        }
      }
      return String.valueOf(object);
    } else if (PrimitiveUtils.isPrimitiveWrapper(object.getClass())) {
      if (object.getClass() == Character.class) {
        if ((Character) object == ' ') {
          return "#32";
        } else if ((Character) object == '\r') {
          return "#13";
        } else if ((Character) object == '\n') {
          return "#10";
        } else if ((Character) object == (char) 0) {
          return "#00";
        }
      }
      return String.valueOf(object);
    } else if (object.getClass().isEnum()) {
      return ((Enum) object).name();
    } else if (object instanceof CharSequence) {
      //比如word stock 数据中有脏字符
      StringBuilder sbuf = new StringBuilder();
      for (char c : object.toString().toCharArray()) {
        if (c == ' ') {
          sbuf.append("#32");
          continue;
        }
        if (c == '\r') {
          sbuf.append("#13");
          continue;
        }
        if (c == '\n') {
          sbuf.append("#10");
          continue;
        }
        if (c == (char) 0) {
          sbuf.append("#00");
          continue;
        }
        sbuf.append(c);
      }
      return sbuf.toString();
    } else if (object instanceof Date) {
      return Long.toString(((Date) object).getTime());
    } else if (object.getClass().isArray()) {
      int len = Array.getLength(object);
      if (len == 0) {
        return "[]";
      }
      Object[] arr;
      if (object.getClass().getComponentType() == boolean.class) {
        boolean[] a = (boolean[]) object;
        arr = new Object[a.length];
        for (int i = 0; i < a.length; i++) {
          arr[i] = a[i];
        }
      } else if (object.getClass().getComponentType() == byte.class) {
        byte[] a = (byte[]) object;
        arr = new Object[a.length];
        for (int i = 0; i < a.length; i++) {
          arr[i] = a[i];
        }
      } else if (object.getClass().getComponentType() == char.class) {
        char[] a = (char[]) object;
        arr = new Object[a.length];
        for (int i = 0; i < a.length; i++) {
          arr[i] = a[i];
        }
      } else if (object.getClass().getComponentType() == short.class) {
        short[] a = (short[]) object;
        arr = new Object[a.length];
        for (int i = 0; i < a.length; i++) {
          arr[i] = a[i];
        }
      } else if (object.getClass().getComponentType() == int.class) {
        int[] a = (int[]) object;
        arr = new Object[a.length];
        for (int i = 0; i < a.length; i++) {
          arr[i] = a[i];
        }
      } else if (object.getClass().getComponentType() == long.class) {
        long[] a = (long[]) object;
        arr = new Object[a.length];
        for (int i = 0; i < a.length; i++) {
          arr[i] = a[i];
        }
      } else if (object.getClass().getComponentType() == float.class) {
        float[] a = (float[]) object;
        arr = new Object[a.length];
        for (int i = 0; i < a.length; i++) {
          arr[i] = a[i];
        }
      } else if (object.getClass().getComponentType() == double.class) {
        double[] a = (double[]) object;
        arr = new Object[a.length];
        for (int i = 0; i < a.length; i++) {
          arr[i] = a[i];
        }
      } else {
        arr = (Object[]) object;
      }
      List<String> s = new ArrayList<>();
      for (Object each : arr) {
        s.add(objectToCacheKey(each));
      }
      return "[" + StringUtils.join(s, ",") + "]";
    } else if (object instanceof Collection) {
      Collection collection = (Collection) object;
      if (collection.isEmpty()) {
        return "[]";
      } else {
        List<String> s = new ArrayList<>();
        for (Object each : collection) {
          s.add(objectToCacheKey(each));
        }
        return "[" + StringUtils.join(s, ",") + "]";
      }
    } else if (object instanceof Map) {
      Map map = (Map) object;
      if (map.isEmpty()) {
        return "[]";
      } else {
        List<String> s = new ArrayList<>();
        for (Object key : map.keySet()) {
          Object value = map.get(key);
          String k = objectToCacheKey(key);
          String v = objectToCacheKey(value);
          s.add(k + "=" + v);
        }
        return "[" + StringUtils.join(s, ",") + "]";
      }
    }
    return String.valueOf(object);
  }
}
