package org.goblinframework.api.compression;

public enum Compressor {

  BZIP2("bzip2"),
  GZIP("gz"),
  DEFLATE("deflate");

  private final String name;

  Compressor(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
