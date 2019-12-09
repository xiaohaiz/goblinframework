package org.goblinframework.dao.mongo.ql;

import org.bson.BsonBoolean;
import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.goblinframework.api.annotation.Singleton;
import org.goblinframework.api.core.Order;
import org.goblinframework.core.util.StringUtils;
import org.goblinframework.dao.ql.Query;

import java.util.Map;

@Singleton
final public class MongoQueryTranslator {

  public static final MongoQueryTranslator INSTANCE = new MongoQueryTranslator();

  public BsonDocument toFilter(Query query) {
    return MongoCriteriaTranslator.INSTANCE.translate(query.getCriteria());
  }

  public BsonDocument toProjection(Query query) {
    Map<String, Boolean> map = query.field().getProjection();
    if (map.isEmpty()) {
      return null;
    }
    BsonDocument projection = new BsonDocument();
    map.entrySet().stream()
        .filter(e -> StringUtils.isNotBlank(e.getKey()))
        .filter(e -> e.getValue() != null)
        .forEach(e -> {
          String f = e.getKey().trim();
          BsonBoolean b = new BsonBoolean(e.getValue());
          projection.put(f, b);
        });
    return projection.isEmpty() ? null : projection;
  }

  public BsonDocument toSort(Query query) {
    if (query.getSort() == null) {
      return null;
    }
    BsonDocument sort = new BsonDocument();
    for (Order order : query.getSort()) {
      sort.put(order.getProperty(), order.isAscending() ? new BsonInt32(1) : new BsonInt32(-1));
    }
    return sort.isEmpty() ? null : sort;
  }

}
