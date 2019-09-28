package org.goblinframework.database.mongo.mapping

import com.fasterxml.jackson.annotation.JsonIgnore
import org.goblinframework.api.annotation.Ignore
import org.goblinframework.api.annotation.Singleton
import org.goblinframework.core.exception.GoblinMappingException
import org.goblinframework.core.util.GoblinField
import org.goblinframework.core.util.ReflectionUtils
import org.goblinframework.database.core.mapping.EntityFieldScanner

@Singleton
class MongoEntityFieldScanner private constructor() : EntityFieldScanner {

  companion object {
    @JvmField val INSTANCE = MongoEntityFieldScanner()
  }

  override fun scan(entityClass: Class<*>): MutableList<GoblinField> {
    val fields = ReflectionUtils.allFieldsIncludingAncestors(entityClass, false, false)
        .map { GoblinField(it) }
        .filterNot { it.isAnnotationPresent(Ignore::class.java) }
        .filterNot { it.isAnnotationPresent(JsonIgnore::class.java) }
        .toMutableList()
    val primitiveFieldNames = fields.filter { it.fieldType.isPrimitive }.map { it.fieldName }.toSet()
    if (primitiveFieldNames.isNotEmpty()) {
      val errMsg = "Primitive field not allowed: entity [${entityClass.name}] has following " +
          "primitive fields: [${primitiveFieldNames.joinToString(",")}]"
      throw GoblinMappingException(errMsg)
    }
    return fields
  }
}