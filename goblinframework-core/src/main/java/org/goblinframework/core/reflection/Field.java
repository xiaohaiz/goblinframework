package org.goblinframework.core.reflection;

import org.goblinframework.core.conversion.ConversionService;
import org.goblinframework.core.util.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Field {

  private final Class<?> clazz;
  private final java.lang.reflect.Field field;
  private final String fieldName;
  private final Class<?> fieldType;
  private final Method getter;
  private final Method setter;

  public Field(@NotNull java.lang.reflect.Field field) {
    this(field.getDeclaringClass(), field);
  }

  public Field(@NotNull Class<?> clazz, @NotNull java.lang.reflect.Field field) {
    this.clazz = clazz;
    this.field = field;
    this.fieldName = field.getName();
    this.fieldType = field.getType();
    this.getter = initializeGetter();
    this.setter = initializeSetter();
  }

  public boolean isAnnotationPresent(@NotNull Class<? extends Annotation> annotationClass) {
    return field.isAnnotationPresent(annotationClass);
  }

  public Class<?> getClazz() {
    return clazz;
  }

  public java.lang.reflect.Field getField() {
    return field;
  }

  public String getFieldName() {
    return fieldName;
  }

  public Class<?> getFieldType() {
    return fieldType;
  }

  public Method getGetter() {
    return getter;
  }

  public Method getSetter() {
    return setter;
  }

  public Object get(@NotNull Object obj) {
    try {
      if (getter != null) {
        return getter.invoke(obj);
      }
      if (!field.isAccessible()) {
        field.setAccessible(true);
      }
      return field.get(obj);
    } catch (IllegalAccessException | InvocationTargetException ex) {
      throw new GoblinReflectionException(ex);
    }
  }

  public void set(@NotNull Object obj, @Nullable Object value) {
    if (value != null && !fieldType.isAssignableFrom(value.getClass())) {
      ConversionService conversionService = ConversionService.INSTANCE;
      if (!conversionService.canConvert(value.getClass(), fieldType)) {
        throw new GoblinReflectionException(value + " is not " + fieldType.getName());
      }
      value = conversionService.convert(value, fieldType);
    }
    try {
      if (setter != null) {
        setter.invoke(obj, value);
        return;
      }
      if (!field.isAccessible()) {
        field.setAccessible(true);
      }
      field.set(obj, value);
    } catch (IllegalAccessException | InvocationTargetException ex) {
      throw new GoblinReflectionException(ex);
    }
  }

  private Method initializeGetter() {
    Method getter = null;
    String prefix = fieldType == boolean.class ? "is" : "get";
    String getterMethodName = prefix + StringUtils.capitalize(fieldName);
    try {
      getter = clazz.getDeclaredMethod(getterMethodName);
      if (!getter.getReturnType().isAssignableFrom(fieldType)) {
        getter = null;
      }
    } catch (NoSuchMethodException ignored) {
      // no getter method found, ignored
    }
    if (getter != null && !getter.isAccessible()) {
      getter.setAccessible(true);
    }
    return getter;
  }

  private Method initializeSetter() {
    Method setter = null;
    String setterMethodName = "set" + StringUtils.capitalize(fieldName);
    try {
      setter = clazz.getDeclaredMethod(setterMethodName, fieldType);
      if (setter.getReturnType() != void.class) {
        setter = null;
      }
    } catch (NoSuchMethodException ignored) {
      // no setter method found, ignored
    }
    if (setter != null && !setter.isAccessible()) {
      setter.setAccessible(true);
    }
    return setter;
  }
}
