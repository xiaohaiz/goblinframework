package org.goblinframework.queue.consumer

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