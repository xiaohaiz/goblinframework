package org.goblinframework.database.core.support;

import org.goblinframework.api.database.CreateTime;
import org.goblinframework.api.database.UpdateTime;
import org.goblinframework.core.container.SpringManagedBean;
import org.goblinframework.core.conversion.ConversionService;
import org.goblinframework.core.exception.GoblinMappingException;
import org.goblinframework.core.util.ClassUtils;
import org.goblinframework.core.util.DateFormatUtils;
import org.goblinframework.database.core.mapping.*;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * NOTE: spring container supported is optional.
 */
abstract public class EntityMappingSupport<E, ID> extends SpringManagedBean {

  protected final EntityMapping entityMapping;

  protected EntityMappingSupport() {
    Class<?> clazz = ClassUtils.filterCglibProxyClass(getClass());
    Type genericSuperClass = clazz.getGenericSuperclass();
    ParameterizedType type = (ParameterizedType) genericSuperClass;
    Type[] typeArgs = type.getActualTypeArguments();
    if (typeArgs.length != 2) {
      throw new GoblinMappingException("Incorrect generic type declaration: " + clazz.getName());
    }
    @SuppressWarnings("unchecked") Class<E> entityClass = (Class<E>) typeArgs[0];
    @SuppressWarnings("unchecked") Class<ID> idClass = (Class<ID>) typeArgs[1];

    EntityMappingBuilder mappingBuilder = getEntityMappingBuilder();
    entityMapping = mappingBuilder.getEntityMapping(entityClass);
    if (entityMapping.idClass != idClass) {
      throw new GoblinMappingException("Incorrect id class declaration [" + idClass.getName()
          + "], actual is [" + entityMapping.idClass.getName() + "]");
    }

    Object mock = entityMapping.newInstance();
    for (EntityField field : entityMapping.asFieldList()) {
      if (field.getValue(mock) != null) {
        throw new GoblinMappingException("No initial value allowed: " + entityClass.getName());
      }
    }
  }

  @NotNull
  abstract protected EntityMappingBuilder getEntityMappingBuilder();

  protected void touchCreateTime(@NotNull E entity, long millis) {
    for (EntityCreateTimeField createTimeField : entityMapping.createTimeFields) {
      Class<?> ft = createTimeField.getField().getFieldType();
      if (ft == String.class) {
        CreateTime annotation = createTimeField.getAnnotation(CreateTime.class);
        assert annotation != null;
        String pattern = annotation.pattern();
        String s = DateFormatUtils.format(millis, pattern);
        createTimeField.getField().set(entity, s);
      } else {
        createTimeField.getField().set(entity, millis);
      }
    }
  }

  protected void touchUpdateTime(@NotNull E entity, long millis) {
    for (EntityUpdateTimeField updateTimeField : entityMapping.updateTimeFields) {
      Class<?> ft = updateTimeField.getField().getFieldType();
      if (ft == String.class) {
        UpdateTime annotation = updateTimeField.getAnnotation(UpdateTime.class);
        assert annotation != null;
        String pattern = annotation.pattern();
        String s = DateFormatUtils.format(millis, pattern);
        updateTimeField.getField().set(entity, s);
      } else {
        updateTimeField.getField().set(entity, millis);
      }
    }
  }

  protected void initializeRevision(@NotNull E entity) {
    EntityRevisionField revisionField = entityMapping.revisionField;
    if (revisionField == null) {
      return;
    }
    ConversionService conversionService = ConversionService.INSTANCE;
    Object revision = conversionService.convert("1", revisionField.getField().getFieldType());
    revisionField.setValue(entity, revision);
  }

  @SuppressWarnings("unchecked")
  protected E newEntityInstance() {
    return (E) entityMapping.newInstance();
  }

  @SuppressWarnings("unchecked")
  protected ID getEntityId(E entity) {
    return (ID) entityMapping.getId(entity);
  }

  protected void setEntityId(E entity, ID id) {
    entityMapping.setId(entity, id);
  }
}
