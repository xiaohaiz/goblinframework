package org.goblinframework.queue.kafka.listener

interface ConsumerRecordListener {

  fun onFetched()

  fun onTransformed()

  fun onDiscarded()

  fun onPublished()

  fun onReceived()

  fun onHandled()

  fun onSuccess()

  fun onFailure()
}