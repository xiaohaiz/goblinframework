package org.goblinframework.database.core.mapping;

import org.goblinframework.api.annotation.ThreadSafe;
import org.goblinframework.api.database.*;
import org.goblinframework.core.exception.GoblinMappingException;
import org.goblinframework.core.service.GoblinManagedBean;
import org.goblinframework.core.service.GoblinManagedObject;
import org.goblinframework.core.util.GoblinField;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@ThreadSafe
@GoblinManagedBean(type = "CORE")
public class EntityMappingBuilder extends GoblinManagedObject implements EntityMappingBuilderMXBean {

  private final EntityFieldScanner scanner;
  private final EntityFieldNameResolver nameResolver;
  private final ReentrantLock lock = new ReentrantLock();
  private final ConcurrentHashMap<Class<?>, EntityMapping> buffer = new ConcurrentHashMap<>();

  public EntityMappingBuilder(@NotNull EntityFieldScanner scanner,
                              @NotNull EntityFieldNameResolver nameResolver) {
    this.scanner = scanner;
    this.nameResolver = nameResolver;
  }

  @NotNull
  public EntityMapping getEntityMapping(@NotNull Class<?> entityClass) {
    EntityMapping mapping = buffer.get(entityClass);
    if (mapping != null) return mapping;
    lock.lock();
    try {
      mapping = buffer.get(entityClass);
      if (mapping != null) return mapping;
      mapping = createEntityMapping(entityClass);
      buffer.put(entityClass, mapping);
      return mapping;
    } finally {
      lock.unlock();
    }
  }

  @Override
  protected void disposeBean() {
    lock.lock();
    try {
      buffer.values().forEach(EntityMapping::dispose);
      buffer.clear();
    } finally {
      lock.unlock();
    }
  }

  @NotNull
  private EntityMapping createEntityMapping(@NotNull Class<?> entityClass) {
    Constructor<?> entityConstructor;
    try {
      entityConstructor = entityClass.getConstructor();
    } catch (NoSuchMethodException ex) {
      throw new GoblinMappingException("[" + entityClass.getName() + "] public no-arguments constructor is required");
    }

    List<GoblinField> allFields = new LinkedList<>(scanner.scan(entityClass));
    List<EntityIdField> idFields = drainEntityIdFields(allFields);
    if (idFields.size() != 1) {
      throw new GoblinMappingException("[" + entityClass.getName() + "] at least/most one Id field is required");
    }
    List<EntityRevisionField> revisionFields = drainEntityRevisionFields(allFields);
    if (revisionFields.size() > 1) {
      throw new GoblinMappingException("[" + entityClass.getName() + "] at most one Revision field is required");
    }

    List<EntityCreateTimeField> createTimeFields = drainCreateTimeFields(allFields);

    List<EntityUpdateTimeField> updateTimeFields = drainUpdateTimeFields(allFields);

    List<EntityEmbedField> embedFields = drainEmbedFields(allFields);

    List<EntityNormalField> normalFields = allFields.stream().map(e -> new EntityNormalField(nameResolver, e)).collect(Collectors.toList());

    // check duplicated name
    Set<String> names = new LinkedHashSet<>();
    List<EntityField> allEntityFields = new ArrayList<>();
    allEntityFields.addAll(idFields);
    allEntityFields.addAll(revisionFields);
    allEntityFields.addAll(createTimeFields);
    allEntityFields.addAll(updateTimeFields);
    allEntityFields.addAll(normalFields);
    allEntityFields.addAll(embedFields);
    for (EntityField field : allEntityFields) {
      String name = field.getName();
      if (!names.add(name)) {
        throw new GoblinMappingException("[" + entityClass.getName() + "] duplicated field name detected: " + name);
      }
    }

    EntityMapping mapping = new EntityMapping();
    mapping.entityClass = entityClass;
    mapping.entityConstructor = entityConstructor;
    mapping.allFieldNames = names;
    mapping.idField = idFields.iterator().next();
    mapping.idClass = mapping.idField.getField().getFieldType();
    mapping.revisionField = revisionFields.stream().findFirst().orElse(null);
    mapping.createTimeFields = createTimeFields;
    mapping.updateTimeFields = updateTimeFields;
    mapping.embedFields = embedFields;
    mapping.normalFields = normalFields;
    return mapping;
  }

  private List<EntityIdField> drainEntityIdFields(List<GoblinField> allFields) {
    List<EntityIdField> idFieldList = new ArrayList<>();
    Iterator<GoblinField> it = allFields.iterator();
    while (it.hasNext()) {
      GoblinField f = it.next();
      if (!f.isAnnotationPresent(Id.class)) {
        continue;
      }
      idFieldList.add(new EntityIdField(nameResolver, f));
      it.remove();
    }
    return idFieldList;
  }

  private List<EntityRevisionField> drainEntityRevisionFields(List<GoblinField> allFields) {
    List<EntityRevisionField> revisionFieldList = new ArrayList<>();
    Iterator<GoblinField> it = allFields.iterator();
    while (it.hasNext()) {
      GoblinField f = it.next();
      if (f.isAnnotationPresent(Revision.class)) {
        revisionFieldList.add(new EntityRevisionField(nameResolver, f));
        it.remove();
      }
    }
    return revisionFieldList;
  }

  private List<EntityCreateTimeField> drainCreateTimeFields(List<GoblinField> allFields) {
    List<EntityCreateTimeField> createTimeFieldList = new ArrayList<>();
    Iterator<GoblinField> it = allFields.iterator();
    while (it.hasNext()) {
      GoblinField f = it.next();
      if (f.isAnnotationPresent(CreateTime.class)) {
        createTimeFieldList.add(new EntityCreateTimeField(nameResolver, f));
        it.remove();
      }
    }
    return createTimeFieldList;
  }

  private List<EntityUpdateTimeField> drainUpdateTimeFields(List<GoblinField> allFields) {
    List<EntityUpdateTimeField> updateTimeFieldList = new ArrayList<>();
    Iterator<GoblinField> it = allFields.iterator();
    while (it.hasNext()) {
      GoblinField f = it.next();
      if (f.isAnnotationPresent(UpdateTime.class)) {
        updateTimeFieldList.add(new EntityUpdateTimeField(nameResolver, f));
        it.remove();
      }
    }
    return updateTimeFieldList;
  }

  private List<EntityEmbedField> drainEmbedFields(List<GoblinField> allFields) {
    List<EntityEmbedField> embedFieldList = new ArrayList<>();
    Iterator<GoblinField> it = allFields.iterator();
    while (it.hasNext()) {
      GoblinField f = it.next();
      if (!f.isAnnotationPresent(Embed.class)) {
        continue;
      }
      it.remove();
      Class<?> embedEntityClass = f.getFieldType();
      for (GoblinField child : scanner.scan(embedEntityClass)) {
        if (child.isAnnotationPresent(Id.class)) {
          throw new GoblinMappingException("[" + embedEntityClass + "] embed entity must not has Id field");
        }
        if (child.isAnnotationPresent(Revision.class)) {
          throw new GoblinMappingException("[" + embedEntityClass + "] embed entity must not has Revision field");
        }
        if (child.isAnnotationPresent(CreateTime.class)) {
          throw new GoblinMappingException("[" + embedEntityClass + "] embed entity must not has CreateTime field");
        }
        if (child.isAnnotationPresent(UpdateTime.class)) {
          throw new GoblinMappingException("[" + embedEntityClass + "] embed entity must not has UpdateTime field");
        }
        if (child.isAnnotationPresent(Embed.class)) {
          throw new GoblinMappingException("[" + embedEntityClass + "] embed entity must not has Embed field");
        }
        embedFieldList.add(new EntityEmbedField(nameResolver, child, f));
      }
    }
    return embedFieldList;
  }
}
