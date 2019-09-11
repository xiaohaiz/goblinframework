package org.goblinframework.core.util;

import org.bson.types.ObjectId;
import org.goblinframework.core.module.spi.RandomProvider;
import org.jetbrains.annotations.NotNull;

import java.security.SecureRandom;
import java.util.Random;

abstract public class RandomUtils {

  private static final Random RANDOM;

  static {
    Random random;
    RandomProvider provider = ServiceInstaller.installedFirst(RandomProvider.class);
    if (provider != null) {
      random = provider.getRandom();
    } else {
      random = initializeRandom();
    }
    RANDOM = random;
  }

  private static Random initializeRandom() {
    try {
      SecureRandom ran = SecureRandom.getInstance("SHA1PRNG");
      byte[] seed = ran.generateSeed(20);
      return new SecureRandom(seed);
    } catch (Exception ex) {
      return new Random();
    }
  }

  @NotNull
  public static Random getRandom() {
    return RANDOM;
  }

  @NotNull
  public static String nextObjectId() {
    return new ObjectId().toHexString();
  }

  public static boolean nextBoolean() {
    return RANDOM.nextBoolean();
  }

  public static int nextInt() {
    return RANDOM.nextInt();
  }

  public static long nextLong() {
    return RANDOM.nextLong();
  }
}
