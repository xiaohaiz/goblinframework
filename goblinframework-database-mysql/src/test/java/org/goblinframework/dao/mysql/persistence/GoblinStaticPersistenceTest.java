package org.goblinframework.dao.mysql.persistence;

import org.goblinframework.api.dao.*;
import org.goblinframework.core.container.SpringManagedBean;
import org.goblinframework.core.util.RandomUtils;
import org.goblinframework.dao.mysql.module.test.RebuildMysqlTable;
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
public class GoblinStaticPersistenceTest extends SpringManagedBean {

  @Table(table = "MOCK_DATA_T")
  public static class MockData implements Serializable {
    private static final long serialVersionUID = -4568367284300289600L;

    @Id(Id.Generator.AUTO_INC) public Long id;
    public String name;
    @CreateTime @Field("CT1") public Instant createTime1;
    @CreateTime @Field("CT2") public Date createTime2;
    @CreateTime @Field("UT1") public Calendar updateTime1;
    @CreateTime @Field("UT2") public Long updateTime2;
    @CreateTime @Field("UT3") public String updateTime3;
    @Revision public Integer revision;
    @Embed public Ext ext;
  }

  public static class Ext implements Serializable {
    private static final long serialVersionUID = -4549264762618662638L;

    @Field("F1") public String field1;
    @Field("F2") public String field2;
    @Field("F3") public String field3;
  }

  @Repository
  @GoblinDatabaseConnection(name = "_ut")
  public static class MockDataPersistence extends GoblinStaticPersistence<MockData, Long> {
  }

  @Inject private MockDataPersistence persistence;

  @Test
  @RebuildMysqlTable(name = "_ut", entity = MockData.class)
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