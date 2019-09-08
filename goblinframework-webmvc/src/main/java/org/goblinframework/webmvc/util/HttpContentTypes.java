package org.goblinframework.webmvc.util;

import kotlin.text.Charsets;
import org.springframework.http.MediaType;

final public class HttpContentTypes {

  public static final String TEXT_HTML;
  public static final String TEXT_PLAIN;
  public static final String APPLICATION_JSON;

  static {
    TEXT_HTML = new MediaType(MediaType.TEXT_HTML, Charsets.UTF_8).toString();
    TEXT_PLAIN = new MediaType(MediaType.TEXT_PLAIN, Charsets.UTF_8).toString();
    APPLICATION_JSON = MediaType.APPLICATION_JSON_UTF8.toString();
  }
}
