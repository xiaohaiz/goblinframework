package org.goblinframework.queue

import java.io.Serializable

class QueueLocation : Serializable, Cloneable {

    companion object {
        val serialVersionUID = 4860482188191310212L
    }

    val queueSystem: QueueSystem
    val queue: String
    val config: String

    constructor(queueSystem: QueueSystem, queue: String, config: String) {
        this.queueSystem = queueSystem
        this.queue = queue
        this.config = config
    }

    constructor(location: String) {
        val split = location.split('/')
        if (split.size == 3) {
            this.queueSystem = QueueSystem.valueOf(split[0])
            this.config = split[1]
            this.queue = split[2]
        } else {
            throw IllegalArgumentException("Invalid queue loacation string")
        }
    }

    override fun toString(): String {
        return "$queueSystem/$config/$queue"
    }
}