package com.mongodb.async.client;

import com.mongodb.MongoDriverInformation;
import com.mongodb.MongoInternalException;
import com.mongodb.connection.Cluster;
import com.mongodb.connection.DefaultClusterFactory;
import com.mongodb.connection.StreamFactory;
import com.mongodb.connection.StreamFactoryFactory;
import org.goblinframework.api.annotation.Compatible;

import static com.mongodb.assertions.Assertions.notNull;
import static com.mongodb.internal.event.EventListenerHelper.getCommandListener;

@Compatible(
    group = "org.mongodb",
    artifact = "mongodb-driver-reactivestreams",
    version = "1.12.0"
)
@SuppressWarnings("deprecation")
final public class GoblinMongoClients {

  public static MongoClient create(final com.mongodb.MongoClientSettings settings) {
    return create(MongoClientSettings.createFromClientSettings(settings));
  }

  private static MongoClient create(final MongoClientSettings settings) {
    return createMongoClient(settings, getStreamFactory(settings, false),
        getStreamFactory(settings, true));
  }

  private static MongoClient createMongoClient(final MongoClientSettings settings,
                                               final StreamFactory streamFactory,
                                               final StreamFactory heartbeatStreamFactory) {
    Cluster cluster = createCluster(settings, streamFactory, heartbeatStreamFactory);
    return new GoblinMongoClientImpl(settings, cluster);
  }

  private static Cluster createCluster(final MongoClientSettings settings,
                                       final StreamFactory streamFactory,
                                       final StreamFactory heartbeatStreamFactory) {
    notNull("settings", settings);
    MongoDriverInformation.Builder builder = MongoDriverInformation.builder();
    return new DefaultClusterFactory().createCluster(settings.getClusterSettings(), settings.getServerSettings(),
        settings.getConnectionPoolSettings(), streamFactory, heartbeatStreamFactory, settings.getCredentialList(),
        getCommandListener(settings.getCommandListeners()), settings.getApplicationName(), builder.driverName("async").build(),
        settings.getCompressorList());
  }

  private static StreamFactory getStreamFactory(final MongoClientSettings settings, final boolean isHeartbeat) {
    StreamFactoryFactory streamFactoryFactory = settings.getStreamFactoryFactory();
    if (streamFactoryFactory == null) {
      throw new MongoInternalException("should not happen");
    }
    return streamFactoryFactory.create(isHeartbeat ? settings.getHeartbeatSocketSettings() : settings.getSocketSettings(),
        settings.getSslSettings());
  }
}
