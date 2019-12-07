package org.goblinframework.database.core.mapping;

import org.goblinframework.dao.core.annotation.DatabaseSystem;

public interface EntityMappingBuilderProvider {

  DatabaseSystem getDatabaseSystem();

  EntityMappingBuilder getEntityMappingBuilder();

}
