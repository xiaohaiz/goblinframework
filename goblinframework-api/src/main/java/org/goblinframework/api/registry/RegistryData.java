package org.goblinframework.api.registry;

import java.io.Serializable;

public class RegistryData<E> implements Serializable {
  private static final long serialVersionUID = -3480030345261281263L;

  public E data;
  public int version;
  public boolean hit;

}
