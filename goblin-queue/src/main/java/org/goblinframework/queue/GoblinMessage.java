package org.goblinframework.queue;

import org.goblinframework.core.util.JsonUtils;
import org.goblinframework.core.util.RandomUtils;
import org.goblinframework.core.util.StringUtils;

import java.io.Serializable;

public class GoblinMessage implements Serializable {

  private static final long serialVersionUID = 8980985171947400566L;

  private String id;

  private Object data;

  private GoblinMessageSerializer serializer = GoblinMessageSerializer.HESSIAN2;

  private GoblinMessage(String id) {
    this.id = id;
  }

  public static GoblinMessage create() {
    return new GoblinMessage(RandomUtils.nextObjectId());
  }

  public GoblinMessage data(Object data) {
    this.data = data;
    return this;
  }

  public GoblinMessage serializer(GoblinMessageSerializer serializer) {
    this.serializer = serializer;
    return this;
  }

  public GoblinMessageSerializer getSerializer() {
    return this.serializer;
  }

  @Override
  public String toString() {
    return StringUtils.formatMessage("[id:{}, serializer:{}, data:{}]", id, serializer, JsonUtils.toJson(data).substring(0, 100));
  }

  public enum GoblinMessageSerializer {
    HESSIAN2,
    JSON,
    FST,
    JAVA,
  }
}
