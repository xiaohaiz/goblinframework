package org.goblinframework.dao.core.support;

import org.goblinframework.core.container.SpringManagedBean;
import org.goblinframework.core.exception.GoblinMappingException;
import org.goblinframework.core.util.ClassUtils;
import org.goblinframework.dao.core.mapping.EntityField;
import org.goblinframework.dao.core.mapping.EntityMapping;
import org.goblinframework.dao.core.mapping.EntityMappingBuilder;
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

  @SuppressWarnings("unchecked")
  protected E newEntityInstance() {
    return (E) entityMapping.newInstance();
  }

  @SuppressWarnings("unchecked")
  protected ID getEntityId(E document) {
    return (ID) entityMapping.getId(document);
  }

  protected void setEntityId(E document, ID id) {
    entityMapping.setId(document, id);
  }
}
