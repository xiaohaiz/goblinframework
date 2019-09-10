package org.goblinframework.webmvc.view

import freemarker.template.Configuration
import org.goblinframework.core.util.ClassUtils
import org.goblinframework.core.util.StringUtils
import org.springframework.beans.factory.InitializingBean
import org.springframework.context.ResourceLoaderAware
import org.springframework.core.io.DefaultResourceLoader
import org.springframework.core.io.ResourceLoader
import org.springframework.http.MediaType
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactory

open class FreemarkerViewResolver : AbstractViewResolver(), InitializingBean, ResourceLoaderAware {

  companion object {
    private val DEFAULT_CONTENT_TYPE = MediaType(MediaType.TEXT_HTML, Charsets.UTF_8)
  }

  private val settings = FreemarkerSetting.setting
  private var templateLoaderPaths: String? = null
  private var contentType = DEFAULT_CONTENT_TYPE
  private var defaultEncoding: String? = null
  private var preferFileSystemAccess = true
  private var resourceLoader: ResourceLoader? = null
  private var configuration: Configuration? = null

  fun setTemplateLoaderPaths(templateLoaderPaths: String) {
    this.templateLoaderPaths = templateLoaderPaths
  }

  fun setContentType(contentType: String) {
    this.contentType = MediaType.parseMediaType(contentType)
  }

  fun setPreferFileSystemAccess(preferFileSystemAccess: Boolean) {
    this.preferFileSystemAccess = preferFileSystemAccess
  }

  fun setAutoInclude(autoInclude: String) {
    settings.setProperty("auto_include", autoInclude)
  }

  fun setAutoImport(autoImport: String) {
    settings.setProperty("auto_import", autoImport)
  }

  fun setBooleanFormat(booleanFormat: String) {
    settings.setProperty("boolean_format", booleanFormat)
  }

  fun setDateFormat(dateFormat: String) {
    settings.setProperty("date_format", dateFormat)
  }

  fun setDatetimeFormat(datetimeFormat: String) {
    settings.setProperty("datetime_format", datetimeFormat)
  }

  fun setDefaultEncoding(defaultEncoding: String) {
    settings.setProperty("default_encoding", defaultEncoding)
  }

  fun setLocale(locale: String) {
    settings.setProperty("locale", locale)
  }

  fun setLocalizedLookup(localizedLookup: Boolean) {
    settings.setProperty("localized_lookup", java.lang.Boolean.toString(localizedLookup))
  }

  fun setNumberFormat(numberFormat: String) {
    settings.setProperty("number_format", numberFormat)
  }

  fun setTagSyntax(tagSyntax: String) {
    settings.setProperty("tag_syntax", tagSyntax)
  }

  fun setTimeFormat(timeFormat: String) {
    settings.setProperty("time_format", timeFormat)
  }

  fun setUrlEscapingCharset(urlEscapingCharset: String) {
    settings.setProperty("url_escaping_charset", urlEscapingCharset)
  }

  fun setWhitespaceStripping(whitespaceStripping: Boolean) {
    settings.setProperty("whitespace_stripping", java.lang.Boolean.toString(whitespaceStripping))
  }

  override fun setResourceLoader(resourceLoader: ResourceLoader?) {
    this.resourceLoader = resourceLoader
  }

  override fun afterPropertiesSet() {
    defaultEncoding = settings.getProperty("default_encoding", Charsets.UTF_8.name())
    val paths = StringUtils.split(templateLoaderPaths, ",")
        .asList()
        .filterNot { StringUtils.isBlank(it) }
        .map { StringUtils.trim(it) }
        .distinct()
        .toList()
    if (paths.isEmpty()) {
      throw IllegalArgumentException(String.format("Invalid 'templateLoaderPaths': %s", templateLoaderPaths))
    }

    val using = if (resourceLoader != null) resourceLoader else DefaultResourceLoader(ClassUtils.getDefaultClassLoader())
    val factory = FreeMarkerConfigurationFactory()
    factory.setTemplateLoaderPaths(*paths.toTypedArray())
    factory.setFreemarkerSettings(settings)
    factory.setDefaultEncoding(defaultEncoding!!)
    factory.setResourceLoader(using!!)
    factory.setPreferFileSystemAccess(preferFileSystemAccess)
    configuration = factory.createConfiguration()
  }

  override fun createView(name: String): View? {
    if (configuration == null) {
      throw IllegalStateException("'afterPropertiesSet' is required")
    }
    val template = configuration!!.getTemplate(name, defaultEncoding) ?: return null
    return FreemarkerView(template, contentType, defaultEncoding!!)
  }
}