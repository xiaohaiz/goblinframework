package org.goblinframework.monitor.message;

public interface TouchableMessageFactory<E extends TouchableMessage> {
  E newInstance();
}
