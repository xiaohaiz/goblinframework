package org.goblinframework.queue.kafka.listener

interface ConsumerRecordListener {

  fun onFetched()

  fun onDiscarded()

  fun onPublished()

  fun onReceived()

  fun onTransformed()

  fun onHandled()

  fun onSuccess()

  fun onFailure()
}