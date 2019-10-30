package org.goblinframework.queue.kafka

import org.bson.types.ObjectId
import java.util.*

class GoblinMessage {

    val id: ObjectId

    val data: Any

    constructor(data: Any) {
        this.data = data
        this.id = ObjectId()
    }

    fun getCreateDate(): Date {
        return id.date
    }
}