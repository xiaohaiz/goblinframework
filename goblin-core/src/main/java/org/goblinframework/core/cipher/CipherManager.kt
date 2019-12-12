package org.goblinframework.core.cipher

import org.apache.commons.pool2.BasePooledObjectFactory
import org.apache.commons.pool2.ObjectPool
import org.apache.commons.pool2.PooledObject
import org.apache.commons.pool2.impl.DefaultPooledObject
import org.apache.commons.pool2.impl.GenericObjectPool
import org.apache.commons.pool2.impl.GenericObjectPoolConfig
import org.goblinframework.api.annotation.Singleton
import java.util.concurrent.locks.ReentrantReadWriteLock
import javax.crypto.Cipher

@Singleton
class CipherManager private constructor() {
  companion object {
    @JvmField val INSTANCE = CipherManager()

    private const val DEFAULT_POOL_MAX_SIZE = 5000
    private const val DEFAULT_POOL_IDLE_SIZE = 3000
  }

  private val poolMap = mutableMapOf<String, ObjectPool<Cipher>>()
  private val rwLock = ReentrantReadWriteLock()

  fun acquireCipher(cipherMode: String): Cipher? {
    return try {
      val pool = getCipherPool(cipherMode)
      pool?.borrowObject()
    } catch (ex: Exception) {
      if (ex is RuntimeException) {
        throw ex
      }
      throw IllegalStateException("failed to acquire AES cipher", ex)
    }
  }

  fun releaseCipher(cipher: Cipher, cipherMode: String) {
    try {
      val pool = getCipherPool(cipherMode)
      pool?.returnObject(cipher)
    } catch (ignored: Exception) {
    }
  }

  fun getCipherPool(cipherMode: String): ObjectPool<Cipher>? {
    rwLock.readLock().lock()
    try {
      val pool = poolMap[cipherMode]
      if (pool != null) {
        return pool
      }
    } finally {
      rwLock.readLock().unlock()
    }
    rwLock.writeLock().lock()
    return try { // 再次尝试取pool
      var pool = poolMap[cipherMode]
      if (pool != null) {
        return pool
      }
      val config = GenericObjectPoolConfig<Cipher>()
      config.maxTotal = DEFAULT_POOL_MAX_SIZE
      config.maxIdle = DEFAULT_POOL_IDLE_SIZE
      config.jmxNamePrefix = cipherMode + "CipherPool"
      pool = GenericObjectPool<Cipher>(PooledCipherFactory(cipherMode), config)
      poolMap[cipherMode] = pool
      pool
    } finally {
      rwLock.writeLock().unlock()
    }
  }

  class PooledCipherFactory(private val cipherMode: String) : BasePooledObjectFactory<Cipher>() {
    @Throws(IllegalStateException::class)
    override fun create(): Cipher {
      return try {
        Cipher.getInstance(cipherMode)
      } catch (ex: Exception) {
        throw java.lang.IllegalStateException("failed to acquire $cipherMode cipher", ex)
      }
    }

    override fun wrap(obj: Cipher): PooledObject<Cipher> {
      return DefaultPooledObject(obj)
    }

  }
}