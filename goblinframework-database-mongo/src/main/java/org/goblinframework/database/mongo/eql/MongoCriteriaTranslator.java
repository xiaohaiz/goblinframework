package org.goblinframework.database.mongo.eql;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.bson.*;
import org.bson.types.ObjectId;
import org.goblinframework.api.annotation.Singleton;
import org.goblinframework.core.util.StringUtils;
import org.goblinframework.database.core.eql.Criteria;
import org.goblinframework.database.core.eql.CriteriaTranslator;
import org.goblinframework.database.core.eql.Operator;
import org.goblinframework.database.core.eql.OperatorValue;
import org.goblinframework.database.mongo.bson.BsonConversionService;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Singleton
final public class MongoCriteriaTranslator implements CriteriaTranslator<BsonDocument> {

  public static final MongoCriteriaTranslator INSTANCE = new MongoCriteriaTranslator();

  @Override
  public BsonDocument translate(@NotNull Criteria criteria) {
    if (criteria.getJoiner() == null) {
      return translateSingleCriteria(criteria);
    } else {
      return translateMultipleCriteria(criteria);
    }
  }

  private BsonDocument translateMultipleCriteria(Criteria criteria) {
    BsonArray array = new BsonArray();
    criteria.getCriteriaChain().stream()
        .map(c -> {
          if (c.getJoiner() == null) {
            return translateSingleCriteria(c);
          } else {
            return translateMultipleCriteria(c);
          }
        })
        .forEach(array::add);
    switch (criteria.getJoiner()) {
      case $and:
        return new BsonDocument("$and", array);
      case $or:
        return new BsonDocument("$or", array);
      case $nor:
        return new BsonDocument("$nor", array);
      default: {
        throw new UnsupportedOperationException();
      }
    }
  }

  private BsonDocument translateSingleCriteria(Criteria criteria) {
    Map<String, OperatorValue> chain = criteria.export();
    BsonDocument document = new BsonDocument();
    for (Map.Entry<String, OperatorValue> entry : chain.entrySet()) {
      String field = entry.getKey();
      OperatorValue operatorValue = entry.getValue();
      BsonDocument bson = translate(field, operatorValue);
      document.putAll(bson);
    }
    return document;
  }

  @SuppressWarnings("unchecked")
  private BsonDocument translate(String field, OperatorValue operatorValue) {
    if (!operatorValue.hasContent()) {
      String message = String.format("No operator found of field '%s'", field);
      throw new IllegalStateException(message);
    }
    if (operatorValue.size() == 1 && operatorValue.containsKey(Operator.$eq)) {
      BsonValue value = toBsonValue(field, operatorValue.get(Operator.$eq));
      return new BsonDocument(field, value);
    }
    BsonDocument document = new BsonDocument();
    document.put(field, new BsonDocument());
    for (Map.Entry<Operator, Object> entry : operatorValue.entrySet()) {
      Operator operator = entry.getKey();
      if (operator == Operator.$not) {
        continue;
      }
      BsonDocument bson = document.getDocument(field);
      switch (operator) {
        case $eq: {
          BsonValue value = toBsonValue(field, entry.getValue());
          bson.put("$eq", value);
          break;
        }
        case $gt: {
          BsonValue value = toBsonValue(field, entry.getValue());
          bson.put("$gt", value);
          break;
        }
        case $gte: {
          BsonValue value = toBsonValue(field, entry.getValue());
          bson.put("$gte", value);
          break;
        }
        case $lt: {
          BsonValue value = toBsonValue(field, entry.getValue());
          bson.put("$lt", value);
          break;
        }
        case $lte: {
          BsonValue value = toBsonValue(field, entry.getValue());
          bson.put("$lte", value);
          break;
        }
        case $ne: {
          BsonValue value = toBsonValue(field, entry.getValue());
          bson.put("$ne", value);
          break;
        }
        case $in: {
          BsonValue value = toBsonValue(field, entry.getValue());
          bson.put("$in", value);
          break;
        }
        case $nin: {
          BsonValue value = toBsonValue(field, entry.getValue());
          bson.put("$nin", value);
          break;
        }
        case $exists: {
          BsonValue value = toBsonValue(field, entry.getValue());
          bson.put("$exists", value);
          break;
        }
        case $type: {
          BsonType bsonType = (BsonType) entry.getValue();
          bson.put("$type", new BsonInt32(bsonType.getValue()));
          break;
        }
        case $mod: {
          ImmutablePair pair = (ImmutablePair) entry.getValue();
          int divisor = (Integer) pair.getLeft();
          int remainder = (Integer) pair.getRight();
          BsonArray array = new BsonArray();
          array.add(new BsonInt32(divisor));
          array.add(new BsonInt32(remainder));
          bson.put("$mod", array);
          break;
        }
        case $regex: {
          BsonValue value = toBsonValue(field, entry.getValue());
          BsonRegularExpression regex = (BsonRegularExpression) value;
          bson.put("$regex", new BsonString(regex.getPattern()));
          if (StringUtils.isNotEmpty(regex.getOptions())) {
            bson.put("$options", new BsonString(regex.getOptions()));
          }
          break;
        }
        case $all: {
          BsonValue value = toBsonValue(field, entry.getValue());
          bson.put("$all", value);
          break;
        }
        case $elemMatch: {
          Criteria c = (Criteria) entry.getValue();
          bson.put("$elemMatch", translate(c));
          break;
        }
        case $size: {
          BsonValue value = toBsonValue(field, entry.getValue());
          bson.put("$size", value);
          break;
        }
        default: {
          throw new UnsupportedOperationException();
        }
      }
    }
    if (operatorValue.containsKey(Operator.$not)) {
      document = document.getDocument(field);
      document = new BsonDocument("$not", document);
      document = new BsonDocument(field, document);
    }
    return document;
  }

  @SuppressWarnings("unchecked")
  private BsonValue toBsonValue(String field, Object value) {
    // NO_VALUE will be ignored
    if (value == Criteria.NULL_VALUE || value == Criteria.NO_VALUE) {
      return new BsonNull();
    }
    Object valueForUse = value;
    if ("_id".equals(field)) {
      if (value instanceof String && ObjectId.isValid((String) value)) {
        valueForUse = new ObjectId((String) value);
      } else if (value instanceof Collection) {
        valueForUse = new LinkedList();
        Collection collection = (Collection) value;
        for (Object each : collection) {
          if (each instanceof String && ObjectId.isValid((String) each)) {
            ObjectId oid = new ObjectId((String) each);
            ((List) valueForUse).add(oid);
          } else {
            ((List) valueForUse).add(each);
          }
        }
      }
    }
    return BsonConversionService.toBson(valueForUse);
  }
}
