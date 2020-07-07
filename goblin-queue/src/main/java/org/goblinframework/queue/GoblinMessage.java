package org.goblinframework.queue;

import org.goblinframework.core.util.JsonUtils;
import org.goblinframework.core.util.RandomUtils;
import org.goblinframework.core.util.StringUtils;

import java.io.Serializable;

public class GoblinMessage implements Serializable {

  private static final long serialVersionUID = 8980985171947400566L;

  private String id;

  private Object data;

  public GoblinMessage() {
    this(RandomUtils.nextObjectId());
  }

  public GoblinMessage(String id) {
    this.id = id;
  }

  public static GoblinMessage create() {
    return new GoblinMessage(RandomUtils.nextObjectId());
  }

  public GoblinMessage data(Object data) {
    this.data = data;
    return this;
  }

  public String getId() {
    return id;
  }

  public Object getData() {
    return data;
  }

  @Override
  public String toString() {
    return StringUtils.formatMessage("[id:{}, data:{}]", id, JsonUtils.toJson(data));
  }
}
