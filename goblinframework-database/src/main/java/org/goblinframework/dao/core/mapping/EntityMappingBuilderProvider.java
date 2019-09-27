package org.goblinframework.dao.core.mapping;

import org.goblinframework.dao.core.GoblinDatabaseSystem;

public interface EntityMappingBuilderProvider {

  GoblinDatabaseSystem getDatabaseSystem();

  EntityMappingBuilder getEntityMappingBuilder();

}
