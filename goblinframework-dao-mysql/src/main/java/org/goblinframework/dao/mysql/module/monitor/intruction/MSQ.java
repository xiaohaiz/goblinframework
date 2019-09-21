package org.goblinframework.dao.mysql.module.monitor.intruction;

import org.goblinframework.core.monitor.InstructionImpl;

import java.util.List;

final public class MSQ extends InstructionImpl {

  public String name;
  public String mode;
  public String operation;
  public List<String> sqlList;

  public MSQ() {
    super(Id.MSQ, Mode.SYN, true, true);
  }

}
