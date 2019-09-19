package org.goblinframework.dao.core.mapping;

import org.goblinframework.core.mbean.GoblinManagedBean;
import org.goblinframework.core.mbean.GoblinManagedObject;
import org.goblinframework.dao.core.mapping.field.*;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Set;

@GoblinManagedBean(type = "DAO")
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

}
