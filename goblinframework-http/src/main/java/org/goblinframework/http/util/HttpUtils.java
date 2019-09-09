package org.goblinframework.http.util;

import kotlin.text.Charsets;
import org.goblinframework.core.util.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.util.LinkedMultiValueMap;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;

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
  public static LinkedHashMap<String, String[]> parseAllQueryString(@Nullable final String qs,
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

  private static String decodeURL(@NotNull final String s,
                                  @NotNull final Charset c) {
    try {
      return URLDecoder.decode(s, c.name());
    } catch (UnsupportedEncodingException ex) {
      throw new UnsupportedOperationException(ex);
    }
  }
}
