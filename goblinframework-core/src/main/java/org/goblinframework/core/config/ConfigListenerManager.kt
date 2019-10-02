package org.goblinframework.core.config

import org.goblinframework.api.function.Disposable
import org.goblinframework.core.reactor.CoreScheduler
import reactor.core.publisher.EmitterProcessor
import reactor.core.publisher.Flux
import java.time.Instant
import java.util.*
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.write

internal class ConfigListenerManager : Disposable {

  private val processor: EmitterProcessor<Instant>
  private val flux: Flux<Instant>
  private val lock = ReentrantReadWriteLock()
  private val listeners = IdentityHashMap<ConfigListener, ConfigListenerSubscriber>()

  init {
    processor = EmitterProcessor.create()
    flux = Flux.from(processor).publishOn(CoreScheduler.INSTANCE.get())
  }

  fun subscribe(listener: ConfigListener) {
    lock.write {
      listeners[listener]?.run { return }
      val subscriber = ConfigListenerSubscriber(listener)
      Flux.from(flux).subscribe(subscriber)
      listeners[listener] = subscriber
    }
  }

  fun unsubscribe(listener: ConfigListener) {
    lock.write { listeners.remove(listener) }?.dispose()
  }

  fun onConfigChanged() {
    processor.onNext(Instant.now())
  }

  override fun dispose() {
    lock.write {
      listeners.values.forEach { it.dispose() }
      listeners.clear()
    }
    processor.dispose()
  }
}
