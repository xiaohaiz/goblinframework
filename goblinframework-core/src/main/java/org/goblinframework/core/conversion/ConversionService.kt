package org.goblinframework.core.conversion

import org.goblinframework.api.core.ServiceInstaller
import org.goblinframework.core.exception.GoblinInitializationException
import org.springframework.context.support.ConversionServiceFactoryBean
import org.springframework.core.convert.TypeDescriptor
import org.springframework.core.convert.converter.Converter

class ConversionService private constructor() : org.springframework.core.convert.ConversionService {

  companion object {
    @JvmField val INSTANCE = ConversionService()
  }

  private val conversionService: org.springframework.core.convert.ConversionService

  init {
    val customizedConverters = ServiceInstaller.asList(Converter::class.java)
    val factoryBean = ConversionServiceFactoryBean()
    if (customizedConverters.isNotEmpty()) {
      factoryBean.setConverters(customizedConverters.toSet())
    }
    factoryBean.afterPropertiesSet()
    conversionService = factoryBean.`object` ?: throw GoblinInitializationException()
  }

  fun getNativeConversionService(): org.springframework.core.convert.ConversionService {
    return conversionService
  }

  override fun <T : Any?> convert(source: Any?, targetType: Class<T>): T? {
    return conversionService.convert(source, targetType)
  }

  override fun convert(source: Any?, sourceType: TypeDescriptor?, targetType: TypeDescriptor): Any? {
    return conversionService.convert(source, sourceType, targetType)
  }

  override fun canConvert(sourceType: Class<*>?, targetType: Class<*>): Boolean {
    return conversionService.canConvert(sourceType, targetType)
  }

  override fun canConvert(sourceType: TypeDescriptor?, targetType: TypeDescriptor): Boolean {
    return conversionService.canConvert(sourceType, targetType)
  }
}