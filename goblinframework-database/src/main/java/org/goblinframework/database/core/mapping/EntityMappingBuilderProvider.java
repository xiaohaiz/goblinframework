package org.goblinframework.database.core.mapping;

import org.goblinframework.database.core.DatabaseSystem;

public interface EntityMappingBuilderProvider {

  DatabaseSystem getDatabaseSystem();

  EntityMappingBuilder getEntityMappingBuilder();

}
