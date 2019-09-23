package org.goblinframework.registry.zookeeper.module.monitor

import org.goblinframework.api.monitor.Instruction
import org.goblinframework.core.monitor.AbstractInstruction

class ZKP : AbstractInstruction(Instruction.Id.ZKP, Instruction.Mode.SYN, true) {

  var operation: String? = null
  var path: String? = null

}
