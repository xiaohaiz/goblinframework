package org.goblinframework.queue;

public enum GoblinMessageSerializer {
  HESSIAN2(1),
  JSON(2),
  FST(3),
  JAVA(4);

  private byte type;

  GoblinMessageSerializer(int type) {
    this.type = (byte) type;
  }

  public byte getType() {
    return this.type;
  }

  public static GoblinMessageSerializer safeParse(byte type) {
    switch (type) {
      case 1:
        return GoblinMessageSerializer.HESSIAN2;
      case 2:
        return GoblinMessageSerializer.JSON;
      case 3:
        return GoblinMessageSerializer.FST;
      case 4:
        return GoblinMessageSerializer.JAVA;
      default:
        return null;
    }
  }
}
