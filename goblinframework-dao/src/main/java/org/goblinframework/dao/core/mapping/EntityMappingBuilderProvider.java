package org.goblinframework.dao.core.mapping;

import org.goblinframework.dao.core.module.DatabaseSystem;

public interface EntityMappingBuilderProvider {

  DatabaseSystem getDatabaseSystem();

  EntityMappingBuilder getEntityMappingBuilder();

}
