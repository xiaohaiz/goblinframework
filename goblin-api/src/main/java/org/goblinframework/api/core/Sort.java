package org.goblinframework.api.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Sort implements Iterable<Order>, Serializable {
  private static final long serialVersionUID = 2684115113172724385L;

  public static final Direction DEFAULT_DIRECTION = Direction.ASC;


  private final List<Order> orders;

  public Sort(Order... orders) {
    this(Arrays.asList(orders));
  }

  public Sort(List<Order> orders) {
    if (null == orders || orders.isEmpty()) {
      throw new IllegalArgumentException("You have to provide at least one sort property to sort by!");
    }
    this.orders = orders;
  }

  public Sort(String... properties) {
    this(DEFAULT_DIRECTION, properties);
  }

  public Sort(Direction direction, String... properties) {
    this(direction, properties == null ? new ArrayList<>() : Arrays.asList(properties));
  }

  public Sort(Direction direction, List<String> properties) {
    if (properties == null || properties.isEmpty()) {
      throw new IllegalArgumentException("You have to provide at least one property to sort by!");
    }
    this.orders = new ArrayList<>(properties.size());
    for (String property : properties) {
      this.orders.add(new Order(direction, property));
    }
  }

  public Sort and(Sort sort) {
    if (sort == null) {
      return this;
    }
    ArrayList<Order> these = new ArrayList<>(this.orders);
    for (Order order : sort) {
      these.add(order);
    }
    return new Sort(these);
  }

  public Order getOrderFor(String property) {
    for (Order order : this) {
      if (order.getProperty().equals(property)) {
        return order;
      }
    }
    return null;
  }

  @Override
  public Iterator<Order> iterator() {
    return this.orders.iterator();
  }

  @Override
  public boolean equals(Object obj) {

    if (this == obj) {
      return true;
    }

    if (!(obj instanceof Sort)) {
      return false;
    }

    Sort that = (Sort) obj;

    return this.orders.equals(that.orders);
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + orders.hashCode();
    return result;
  }

  @Override
  public String toString() {
    if (orders == null || orders.isEmpty()) {
      return "";
    }
    StringBuilder sbuf = new StringBuilder();
    for (Order order : orders) {
      sbuf.append(order).append(",");
    }
    sbuf.setLength(sbuf.length() - 1);
    return sbuf.toString();
  }

}
