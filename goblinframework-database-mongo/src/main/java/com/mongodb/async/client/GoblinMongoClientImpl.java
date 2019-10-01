package com.mongodb.async.client;

import com.mongodb.connection.Cluster;
import org.goblinframework.api.annotation.Compatible;

import java.io.Closeable;

@Compatible(
    group = "org.mongodb",
    artifact = "mongodb-driver-async",
    version = "3.11.0"
)
@SuppressWarnings("deprecation")
class GoblinMongoClientImpl extends MongoClientImpl {

  GoblinMongoClientImpl(MongoClientSettings settings, Cluster cluster) {
    super(settings, cluster, (Closeable) null);
  }
}
