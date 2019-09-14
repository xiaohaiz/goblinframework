package org.goblinframework.core.transcoder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.goblinframework.core.exception.GoblinTranscodingException;
import org.goblinframework.core.util.JsonUtils;

import java.io.Serializable;

public class DecodedObject implements Serializable {
  private static final long serialVersionUID = 1697647414761808900L;

  public Object decoded;
  public byte serializer;

  public DecodedObject() {
  }

  public DecodedObject(Object decoded, byte serializer) {
    this.decoded = decoded;
    this.serializer = serializer;
  }

  public Object mapTo() {
    ObjectMapper mapper = JsonUtils.getDefaultObjectMapper();
    JsonNode root = null;
    try {
      if (decoded instanceof byte[]) {
        root = mapper.readTree((byte[]) decoded);
      } else if (decoded instanceof String) {
        root = mapper.readTree((String) decoded);
      }
    } catch (Exception ignore) {
    }
    if (root == null || !root.isObject()) {
      throw new GoblinTranscodingException("Decoded object is not JSON object");
    }
    JsonNode idNode = root.get("_id");
    if (idNode == null || idNode.isNull()) {
      throw new GoblinTranscodingException("Decoded object has no valid _id field");
    }
    String id = idNode.asText();
    Class<?> type = DecodedObjectManager.INSTANCE.typeOf(id);
    if (type == null) {
      throw new GoblinTranscodingException("Decoded object [" + id + "] not registered");
    }
    try {
      return mapper.readValue(root.traverse(), type);
    } catch (Exception ex) {
      throw new GoblinTranscodingException(ex);
    }
  }
}
