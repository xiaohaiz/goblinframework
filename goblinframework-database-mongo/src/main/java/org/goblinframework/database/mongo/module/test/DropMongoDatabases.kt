package org.goblinframework.database.mongo.module.test

@MustBeDocumented
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(AnnotationRetention.RUNTIME)
annotation class DropMongoDatabases(vararg val value: DropMongoDatabase)
