package org.goblinframework.dao.mongo.persistence.internal;

import com.mongodb.MongoNamespace;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.util.LinkedMultiValueMap;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

abstract public class MongoPersistenceNamespaceSupport<E, ID> extends MongoPersistenceCollectionSupport<E, ID> {

  protected MongoPersistenceNamespaceSupport() {
  }

  @NotNull
  protected MongoNamespace getEntityNamespace(@Nullable E entity) {
    String database = getEntityDatabaseName(entity);
    String collection = getEntityCollectionName(entity);
    return new MongoNamespace(database, collection);
  }

  @NotNull
  protected MongoNamespace getIdNamespace(@Nullable ID id) {
    E entity = null;
    if (id != null) {
      entity = newEntityInstance();
      setEntityId(entity, id);
    }
    return getEntityNamespace(entity);
  }

  @NotNull
  protected LinkedMultiValueMap<MongoNamespace, E> groupEntities(@NotNull Collection<E> entities) {
    LinkedMultiValueMap<MongoNamespace, E> grouped = new LinkedMultiValueMap<>();
    List<E> entityList = entities.stream().filter(Objects::nonNull).collect(Collectors.toList());
    for (E entity : entityList) {
      MongoNamespace namespace = getEntityNamespace(entity);
      grouped.add(namespace, entity);
    }
    return grouped;
  }

  @NotNull
  protected LinkedMultiValueMap<MongoNamespace, ID> groupIds(@NotNull Collection<ID> ids) {
    LinkedMultiValueMap<MongoNamespace, ID> grouped = new LinkedMultiValueMap<>();
    List<ID> idList = ids.stream().filter(Objects::nonNull).collect(Collectors.toList());
    for (ID id : idList) {
      MongoNamespace namespace = getIdNamespace(id);
      grouped.add(namespace, id);
    }
    return grouped;
  }
}
