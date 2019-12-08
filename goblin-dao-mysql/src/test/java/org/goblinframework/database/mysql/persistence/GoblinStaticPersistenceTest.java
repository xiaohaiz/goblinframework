package org.goblinframework.database.mysql.persistence;

import org.goblinframework.api.dao.*;
import org.goblinframework.core.container.SpringContainerObject;
import org.goblinframework.core.util.RandomUtils;
import org.goblinframework.dao.core.annotation.GoblinConnection;
import org.goblinframework.dao.mysql.annotation.GoblinTable;
import org.goblinframework.test.runner.GoblinTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.ContextConfiguration;

import javax.inject.Inject;
import java.io.Serializable;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

@RunWith(GoblinTestRunner.class)
@ContextConfiguration("/UT.xml")
public class GoblinStaticPersistenceTest extends SpringContainerObject {

  public static class MockData implements Serializable {
    private static final long serialVersionUID = -4568367284300289600L;

    @GoblinId(GoblinId.Generator.AUTO_INC) public Long id;
    public String name;
    @CreateTime @GoblinField("CT1") public Instant createTime1;
    @CreateTime @GoblinField("CT2") public Date createTime2;
    @CreateTime @GoblinField("UT1") public Calendar updateTime1;
    @CreateTime @GoblinField("UT2") public Long updateTime2;
    @CreateTime @GoblinField("UT3") public String updateTime3;
    @GoblinRevision public Integer revision;
    @Embed public Ext ext;
  }

  public static class Ext implements Serializable {
    private static final long serialVersionUID = -4549264762618662638L;

    @GoblinField("F1") public String field1;
    @GoblinField("F2") public String field2;
    @GoblinField("F3") public String field3;
  }

  @Repository
  @GoblinConnection(name = "_ut")
  @GoblinTable(table = "MOCK_DATA_T")
  public static class MockDataPersistence extends GoblinStaticPersistence<MockData, Long> {
  }

  @Inject private MockDataPersistence persistence;

  @Test
  public void persistence() {
    MockData data = new MockData();
    data.name = RandomUtils.nextObjectId();
    data.ext = new Ext();
    data.ext.field1 = "foo";
    data.ext.field2 = "bar";
    data.ext.field3 = "gee";
    persistence.insert(data);

    Long id = data.id;
    assertTrue(persistence.exists(id));

    data = persistence.load(id);
    assertNotNull(data);

    data = new MockData();
    data.id = id;
    data.ext = new Ext();
    data.ext.field3 = "GEE";
    boolean ret = persistence.replace(data);
    assertTrue(ret);

    data = persistence.load(id);
    assertNotNull(data);
    assertEquals("GEE", data.ext.field3);

    ret = persistence.delete(id);
    assertTrue(ret);
    assertFalse(persistence.exists(id));

    data = new MockData();
    ret = persistence.upsert(data);
    assertTrue(ret);
    id = data.id;

    data = new MockData();
    data.id = id;
    data.name = "N";
    persistence.upsert(data);

    data = persistence.load(id);
    assertNotNull(data);
    assertEquals("N", data.name);
  }
}