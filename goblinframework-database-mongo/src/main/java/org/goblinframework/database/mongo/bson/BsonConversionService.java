package org.goblinframework.database.mongo.bson;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.ByteBufUtil;
import org.bson.*;
import org.bson.io.BasicOutputBuffer;
import org.bson.types.ObjectId;
import org.goblinframework.api.function.ValueWrapper;
import org.goblinframework.core.mapper.JsonMapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.time.Instant;
import java.util.*;

abstract public class BsonConversionService {

  private static final String DEFAULT_KEY = "BsonConversionService";

  @NotNull
  public static BsonValue toBson(@Nullable Object object) {
    if (object == null) {
      return new BsonNull();                              // null -> BsonNull
    }
    if (object instanceof BsonValue) {
      return (BsonValue) object;
    }
    if (object instanceof Boolean) {
      boolean b = (Boolean) object;                       // Boolean -> BsonBoolean
      return new BsonBoolean(b);
    }
    if (object instanceof Byte) {
      byte b = (Byte) object;                             // Byte -> BsonInt32
      return new BsonInt32(b);
    }
    if (object instanceof Character) {
      char c = (Character) object;                        // Character -> BsonString
      return new BsonString(Character.toString(c));
    }
    if (object instanceof Double) {
      double d = (Double) object;                         // Double -> BsonDouble
      return new BsonDouble(d);
    }
    if (object instanceof Float) {
      float f = (Float) object;                           // Float -> BsonDouble
      return new BsonDouble(f);
    }
    if (object instanceof Integer) {
      int i = (Integer) object;                           // Integer -> BsonInt32
      return new BsonInt32(i);
    }
    if (object instanceof Long) {
      long l = (Long) object;                             // Long -> BsonInt64
      return new BsonInt64(l);
    }
    if (object instanceof Short) {
      short s = (Short) object;                           // Short -> BsonInt32
      return new BsonInt32(s);
    }
    if (object instanceof String) {
      String s = (String) object;                         // String -> BsonString
      return new BsonString(s);
    }
    if (object instanceof ObjectId) {
      ObjectId o = (ObjectId) object;                     // ObjectId -> BsonObjectId
      return new BsonObjectId(o);
    }
    if (object instanceof Date) {
      Date d = (Date) object;                             // Date -> BsonDateTime
      return new BsonDateTime(d.getTime());
    }
    if (object instanceof Instant) {
      Instant i = (Instant) object;
      return new BsonDateTime(i.toEpochMilli());         // Instant -> BsonDateTime
    }
    if (object instanceof Calendar) {
      Calendar c = (Calendar) object;
      return new BsonDateTime(c.getTimeInMillis());     // Calendar -> BsonDateTime
    }
    if (object instanceof Enum) {                       // Enum -> BsonString
      String s = ((Enum) object).name();
      return new BsonString(s);
    }
    LinkedHashMap<String, Object> map = new LinkedHashMap<>();
    map.put(DEFAULT_KEY, object);
    BsonDocument document = toBson(map);
    return document.get(DEFAULT_KEY);
  }

  public static <T> T toObject(@Nullable BsonDocument document, @NotNull Class<T> clazz) {
    TypeFactory factory = BsonMapper.getDefaultObjectMapper().getTypeFactory();
    return toObject(document, factory.constructType(clazz));
  }

  public static <T> T toObject(@Nullable BsonDocument document, @NotNull JavaType type) {
    if (document == null) {
      return null;
    }
    try (BsonDocumentReader reader = new BsonDocumentReader(document);
         BasicOutputBuffer output = new BasicOutputBuffer();
         BsonBinaryWriter writer = new BsonBinaryWriter(output)) {
      writer.pipe(reader);
      writer.flush();
      byte[] bytes = output.toByteArray();
      return BsonMapper.getDefaultObjectMapper().readValue(bytes, type);
    } catch (Exception ex) {
      throw new GoblinBsonException(ex);
    }
  }

  public static <T> List<T> toList(@Nullable BsonArray array, @NotNull Class<T> elementType) {
    if (array == null) {
      return Collections.emptyList();
    }
    BsonDocument document = new BsonDocument("value", array);
    ObjectMapper mapper = JsonMapper.getDefaultObjectMapper();
    JavaType jt = mapper.getTypeFactory().constructParametricType(ListValueWrapper.class, elementType);
    ListValueWrapper<T> wrapper = toObject(document, jt);
    return wrapper.getValue();
  }

  private static BsonDocument toBson(LinkedHashMap<String, Object> map) {
    byte[] bs;
    ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
    try {
      ByteBufOutputStream bos = new ByteBufOutputStream(buf);
      BsonMapper.getDefaultObjectMapper().writeValue((OutputStream) bos, map);
      bs = ByteBufUtil.getBytes(buf);
    } catch (Exception ex) {
      throw new GoblinBsonException(ex);
    } finally {
      buf.release();
    }
    BsonDocument document = new BsonDocument();
    try (BsonBinaryReader reader = new BsonBinaryReader(ByteBuffer.wrap(bs));
         BsonDocumentWriter writer = new BsonDocumentWriter(document)) {
      writer.pipe(reader);
      writer.flush();
      return document;
    }
  }

  final public static class ListValueWrapper<E> implements ValueWrapper<LinkedList<E>> {

    private LinkedList<E> value;

    public void setValue(@Nullable LinkedList<E> value) {
      this.value = value;
    }

    @Nullable
    @Override
    public LinkedList<E> getValue() {
      return value;
    }
  }
}
