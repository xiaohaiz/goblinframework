package org.goblinframework.database.core.mapping;

import org.goblinframework.database.core.GoblinDatabaseSystem;

public interface EntityMappingBuilderProvider {

  GoblinDatabaseSystem getDatabaseSystem();

  EntityMappingBuilder getEntityMappingBuilder();

}
