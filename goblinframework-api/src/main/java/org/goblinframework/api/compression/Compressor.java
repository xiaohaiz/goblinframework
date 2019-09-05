package org.goblinframework.api.compression;

public enum Compressor {

  BZIP2((byte) 1, "bzip2"),
  GZIP((byte) 2, "gz"),
  DEFLATE((byte) 3, "deflate");

  private final byte id;
  private final String name;

  Compressor(byte id, String name) {
    this.id = id;
    this.name = name;
  }

  public byte getId() {
    return id;
  }

  public String getName() {
    return name;
  }
}
