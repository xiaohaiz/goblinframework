package org.goblinframework.dao.core.mapping;

import org.goblinframework.dao.core.annotation.GoblinDatabaseSystem;

public interface EntityMappingBuilderProvider {

  GoblinDatabaseSystem getDatabaseSystem();

  EntityMappingBuilder getEntityMappingBuilder();

}
