package org.goblinframework.dao.mapping;

import org.goblinframework.api.core.GoblinException;
import org.goblinframework.core.service.GoblinManagedBean;
import org.goblinframework.core.service.GoblinManagedObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@GoblinManagedBean(type = "Dao")
public class EntityMapping extends GoblinManagedObject implements EntityMappingMXBean {

  public Class<?> entityClass;
  public Class<?> idClass;
  public Constructor<?> entityConstructor;
  public Set<String> allFieldNames;
  public EntityIdField idField;
  public EntityRevisionField revisionField;
  public List<EntityCreateTimeField> createTimeFields;
  public List<EntityUpdateTimeField> updateTimeFields;
  public List<EntityEmbedField> embedFields;
  public List<EntityNormalField> normalFields;

  public List<EntityField> asFieldList() {
    List<EntityField> fields = new LinkedList<>();
    if (idField != null) {
      fields.add(idField);
    }
    if (revisionField != null) {
      fields.add(revisionField);
    }
    fields.addAll(createTimeFields);
    fields.addAll(updateTimeFields);
    fields.addAll(embedFields);
    fields.addAll(normalFields);
    return fields;
  }

  public Object newInstance() {
    try {
      return entityConstructor.newInstance();
    } catch (InvocationTargetException ex) {
      throw new GoblinException(ex.getTargetException());
    } catch (Exception ex) {
      throw new GoblinException(ex);
    }
  }

  public Object getId(Object entity) {
    return idField.getField().get(entity);
  }

  public void setId(Object entity, Object id) {
    idField.getField().set(entity, id);
  }

  @NotNull
  @Override
  public String getIdFieldName() {
    return idField.getName();
  }

  @Nullable
  @Override
  public String getRevisionFieldName() {
    return revisionField == null ? null : revisionField.getName();
  }
}
