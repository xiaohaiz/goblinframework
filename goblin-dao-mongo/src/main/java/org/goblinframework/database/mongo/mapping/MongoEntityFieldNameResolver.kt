package org.goblinframework.database.mongo.mapping

import com.fasterxml.jackson.annotation.JsonProperty
import org.goblinframework.api.annotation.Singleton
import org.goblinframework.api.dao.GoblinField
import org.goblinframework.api.dao.GoblinId
import org.goblinframework.core.util.StringUtils
import org.goblinframework.database.core.mapping.EntityField
import org.goblinframework.database.core.mapping.EntityFieldNameResolver

@Singleton
class MongoEntityFieldNameResolver private constructor() : EntityFieldNameResolver {

  companion object {
    @JvmField val INSTANCE = MongoEntityFieldNameResolver()
  }

  override fun resolve(field: EntityField): String {
    field.getAnnotation(GoblinId::class.java)?.run { return "_id" }
    var name = field.getAnnotation(GoblinField::class.java)?.value
    if (StringUtils.isNotBlank(name)) {
      return StringUtils.trim(name)
    }
    name = field.getAnnotation(JsonProperty::class.java)?.value
    if (StringUtils.isNotBlank(name)) {
      return StringUtils.trim(name)
    }
    return field.field.fieldName
  }
}