package org.goblinframework.database.mongo.eql;

import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.goblinframework.api.annotation.Singleton;
import org.goblinframework.core.util.ArrayUtils;
import org.goblinframework.database.core.eql.*;
import org.goblinframework.database.mongo.bson.BsonConversionService;

import java.util.Map;

@Singleton
final public class MongoUpdateTranslator {

  public static final MongoUpdateTranslator INSTANCE = new MongoUpdateTranslator();

  public BsonDocument translate(Update update) {
    Map<Operator, FieldValue> chain = update.export();
    if (chain.isEmpty()) {
      return new BsonDocument();
    }
    BsonDocument document = new BsonDocument();
    chain.entrySet()
        .forEach(e -> {
          Operator operator = e.getKey();
          FieldValue fieldValue = e.getValue();
          switch (operator) {
            case $inc: {
              if (!document.containsKey("$inc")) {
                document.put("$inc", new BsonDocument());
              }
              BsonDocument bson = document.getDocument("$inc");
              for (Map.Entry<String, Object> entry : fieldValue.entrySet()) {
                bson.put(entry.getKey(), BsonConversionService.toBson(entry.getValue()));
              }
              break;
            }
            case $mul: {
              if (!document.containsKey("$mul")) {
                document.put("$mul", new BsonDocument());
              }
              BsonDocument bson = document.getDocument("$mul");
              for (Map.Entry<String, Object> entry : fieldValue.entrySet()) {
                bson.put(entry.getKey(), BsonConversionService.toBson(entry.getValue()));
              }
              break;
            }
            case $setOnInsert: {
              if (!document.containsKey("$setOnInsert")) {
                document.put("$setOnInsert", new BsonDocument());
              }
              BsonDocument bson = document.getDocument("$setOnInsert");
              for (Map.Entry<String, Object> entry : fieldValue.entrySet()) {
                bson.put(entry.getKey(), BsonConversionService.toBson(entry.getValue()));
              }
              break;
            }
            case $set: {
              if (!document.containsKey("$set")) {
                document.put("$set", new BsonDocument());
              }
              BsonDocument bson = document.getDocument("$set");
              for (Map.Entry<String, Object> entry : fieldValue.entrySet()) {
                bson.put(entry.getKey(), BsonConversionService.toBson(entry.getValue()));
              }
              break;
            }
            case $unset: {
              if (!document.containsKey("$unset")) {
                document.put("$unset", new BsonDocument());
              }
              BsonDocument bson = document.getDocument("$unset");
              for (Map.Entry<String, Object> entry : fieldValue.entrySet()) {
                bson.put(entry.getKey(), BsonConversionService.toBson(entry.getValue()));
              }
              break;
            }
            case $min: {
              if (!document.containsKey("$min")) {
                document.put("$min", new BsonDocument());
              }
              BsonDocument bson = document.getDocument("$min");
              for (Map.Entry<String, Object> entry : fieldValue.entrySet()) {
                bson.put(entry.getKey(), BsonConversionService.toBson(entry.getValue()));
              }
              break;
            }
            case $max: {
              if (!document.containsKey("$max")) {
                document.put("$max", new BsonDocument());
              }
              BsonDocument bson = document.getDocument("$max");
              for (Map.Entry<String, Object> entry : fieldValue.entrySet()) {
                bson.put(entry.getKey(), BsonConversionService.toBson(entry.getValue()));
              }
              break;
            }
            case $currentDate: {
              if (!document.containsKey("$currentDate")) {
                document.put("$currentDate", new BsonDocument());
              }
              BsonDocument bson = document.getDocument("$currentDate");
              for (Map.Entry<String, Object> entry : fieldValue.entrySet()) {
                bson.put(entry.getKey(), BsonConversionService.toBson(entry.getValue()));
              }
              break;
            }
            case $addToSet: {
              if (!document.containsKey("$addToSet")) {
                document.put("$addToSet", new BsonDocument());
              }
              BsonDocument bson = document.getDocument("$addToSet");
              for (Map.Entry<String, Object> entry : fieldValue.entrySet()) {
                String field = entry.getKey();
                Object[] values = (Object[]) entry.getValue();
                if (ArrayUtils.isEmpty(values)) {
                  throw new IllegalArgumentException("No value specified for $addToSet operator");
                }
                if (values.length == 1) {
                  bson.put(field, BsonConversionService.toBson(values[0]));
                } else {
                  BsonArray array = (BsonArray) BsonConversionService.toBson(values);
                  bson.put(field, new BsonDocument("$each", array));
                }
              }
              break;
            }
            case $pop: {
              if (!document.containsKey("$pop")) {
                document.put("$pop", new BsonDocument());
              }
              BsonDocument bson = document.getDocument("$pop");
              for (Map.Entry<String, Object> entry : fieldValue.entrySet()) {
                String field = entry.getKey();
                Position position = (Position) entry.getValue();
                switch (position) {
                  case FIRST: {
                    bson.put(field, new BsonInt32(-1));
                    break;
                  }
                  case LAST: {
                    bson.put(field, new BsonInt32(1));
                    break;
                  }
                  default: {
                    throw new IllegalArgumentException("No position specified for $pop operator");
                  }
                }
              }
              break;
            }
            case $pullAll: {
              if (!document.containsKey("$pullAll")) {
                document.put("$pullAll", new BsonDocument());
              }
              BsonDocument bson = document.getDocument("$pullAll");
              for (Map.Entry<String, Object> entry : fieldValue.entrySet()) {
                String field = entry.getKey();
                Object[] values = (Object[]) entry.getValue();
                bson.put(field, BsonConversionService.toBson(values));
              }
              break;
            }
            case $pull: {
              if (!document.containsKey("$pull")) {
                document.put("$pull", new BsonDocument());
              }
              BsonDocument bson = document.getDocument("$pull");
              Criteria criteria = (Criteria) fieldValue.values().iterator().next();
              bson.putAll(MongoCriteriaTranslator.INSTANCE.translate(criteria));
              break;
            }
            case $push: {
              if (!document.containsKey("$push")) {
                document.put("$push", new BsonDocument());
              }
              BsonDocument bson = document.getDocument("$push");
              for (Map.Entry<String, Object> entry : fieldValue.entrySet()) {
                String field = entry.getKey();
                Object[] values = (Object[]) entry.getValue();
                if (values.length == 1) {
                  bson.put(field, BsonConversionService.toBson(values[0]));
                } else {
                  BsonArray array = (BsonArray) BsonConversionService.toBson(values);
                  bson.put(field, new BsonDocument("$each", array));
                }
              }
              break;
            }
            default: {
              throw new UnsupportedOperationException();
            }
          }
        });
    return document;
  }
}
