package org.goblinframework.queue.module.monitor

import org.goblinframework.core.monitor.AbstractInstruction
import org.goblinframework.core.monitor.Instruction.Id
import org.goblinframework.core.monitor.Instruction.Mode
import org.goblinframework.core.monitor.InstructionTranslator
import org.goblinframework.queue.QueueLocation
import org.goblinframework.queue.QueueSystem

/**
 * @author changyuan.liu
 * @since 2020/7/7
 */
class PMG constructor(location: QueueLocation) : AbstractInstruction(Id.PMG, Mode.ASY, true) {

    var system: QueueSystem = location.queueSystem
    var queue: String = location.queue
    var config: String = location.config

    override fun translator(): InstructionTranslator {
        return InstructionTranslator { "$system $config $queue" }
    }
}