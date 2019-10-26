package org.goblinframework.queue.rabbitmq

import org.goblinframework.core.util.RandomUtils

class GoblinMessage {

    val id: String

    val data: Any

    constructor(data: Any) {
        this.data = data
        this.id = RandomUtils.nextObjectId()
    }
}