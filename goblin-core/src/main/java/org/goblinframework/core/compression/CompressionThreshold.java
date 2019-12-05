package org.goblinframework.core.compression;

public enum CompressionThreshold {

  NONE(0),
  _1K(1),
  _2K(2),
  _4K(4),
  _8K(8),
  _16K(16),
  _32K(32),
  _64K(64),
  _128K(128),
  _512K(512),
  _1M(1024),
  _2M(2048),
  _4M(4096),
  _8M(8192);

  private final int size;

  public int getSize() {
    return size;
  }

  CompressionThreshold(int k) {
    this.size = k * 1024;
  }

}
