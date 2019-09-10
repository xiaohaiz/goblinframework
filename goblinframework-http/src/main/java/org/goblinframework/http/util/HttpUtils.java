package org.goblinframework.http.util;

import kotlin.text.Charsets;
import org.goblinframework.core.util.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.util.LinkedMultiValueMap;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.*;

abstract public class HttpUtils {

  public static boolean isMalformedPath(@Nullable final String path) {
    return path == null || path.contains("//") || path.contains("..");
  }

  @Nullable
  public static String compactContinuousSlashes(@Nullable final String path) {
    if (path == null) {
      return null;
    }
    String s = path;
    if (StringUtils.contains(s, "//")) {
      while (StringUtils.contains(s, "//")) {
        s = StringUtils.replace(s, "//", "/");
      }
    }
    if ("/".equals(s)) {
      return "/";
    }
    if (StringUtils.endsWith(s, "/")) {
      s = s.substring(0, s.length() - 1);
    }
    return s;
  }

  @NotNull
  public static String buildQueryString(@Nullable final Map<String, Object> m) {
    return buildQueryString(m, Charsets.UTF_8);
  }

  @NotNull
  public static String buildQueryString(@Nullable final Map<String, Object> m,
                                        @Nullable final Charset charset) {
    if (m == null || m.isEmpty()) {
      return StringUtils.EMPTY;
    }
    Charset c = (charset == null ? Charsets.UTF_8 : charset);
    StringBuilder sbuf = new StringBuilder();
    for (Map.Entry<String, Object> entry : m.entrySet()) {
      String key = entry.getKey();
      Object value = entry.getValue();
      if (value == null) {
        continue;
      }
      String k = encodeURL(key, c);
      List<String> vs = new LinkedList<>();
      if (value instanceof Collection) {
        for (Object o : (Collection) value) {
          vs.add(encodeURL(o.toString(), c));
        }
      } else if (value.getClass().isArray()) {
        int len = Array.getLength(value);
        for (int i = 0; i < len; i++) {
          vs.add(encodeURL(Array.get(value, i).toString(), c));
        }
      } else {
        vs.add(encodeURL(value.toString(), c));
      }
      if (vs.isEmpty()) {
        continue;
      }
      for (String v : vs) {
        sbuf.append(k).append("=").append(v).append("&");
      }
    }
    if (sbuf.length() > 0) {
      sbuf.setLength(sbuf.length() - 1);
    }
    return sbuf.toString();
  }

  @NotNull
  public static LinkedHashMap<String, String> parseQueryString(@Nullable final String qs) {
    return parseQueryString(qs, Charsets.UTF_8);
  }

  @NotNull
  public static LinkedHashMap<String, String> parseQueryString(@Nullable final String qs,
                                                               @Nullable final Charset charset) {
    if (StringUtils.isBlank(qs)) {
      return new LinkedHashMap<>();
    }
    LinkedHashMap<String, String> m = new LinkedHashMap<>();
    Charset c = (charset == null ? Charsets.UTF_8 : charset);
    String[] pairs = StringUtils.split(qs, "&");
    for (String pair : pairs) {
      int idx = pair.indexOf("=");
      String key = decodeURL(pair.substring(0, idx), c);
      String value = decodeURL(pair.substring(idx + 1), c);
      m.putIfAbsent(key, value);
    }
    return m;
  }

  @NotNull
  public static LinkedHashMap<String, String[]> parseMultiQueryString(@Nullable final String qs) {
    return parseMultiQueryString(qs, Charsets.UTF_8);
  }

  @NotNull
  public static LinkedHashMap<String, String[]> parseMultiQueryString(@Nullable final String qs,
                                                                      @Nullable final Charset charset) {
    if (StringUtils.isBlank(qs)) {
      return new LinkedHashMap<>();
    }
    Charset c = (charset == null ? Charsets.UTF_8 : charset);
    LinkedMultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    String[] pairs = StringUtils.split(qs, "&");
    for (String pair : pairs) {
      int idx = pair.indexOf("=");
      String key = decodeURL(pair.substring(0, idx), c);
      String value = decodeURL(pair.substring(idx + 1), c);
      map.add(key, value);
    }
    LinkedHashMap<String, String[]> result = new LinkedHashMap<>();
    map.forEach((key, value) -> result.put(key, value.toArray(new String[0])));
    return result;
  }

  private static String encodeURL(@NotNull final String s,
                                  @NotNull final Charset c) {
    try {
      return URLEncoder.encode(s, c.name());
    } catch (UnsupportedEncodingException ex) {
      throw new UnsupportedOperationException(ex);
    }
  }


  private static String decodeURL(@NotNull final String s,
                                  @NotNull final Charset c) {
    try {
      return URLDecoder.decode(s, c.name());
    } catch (UnsupportedEncodingException ex) {
      throw new UnsupportedOperationException(ex);
    }
  }
}
