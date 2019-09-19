package org.goblinframework.dao.mysql.persistence;

import org.goblinframework.api.annotation.Id;
import org.goblinframework.api.annotation.Table;
import org.goblinframework.core.container.SpringManagedBean;
import org.goblinframework.core.util.RandomUtils;
import org.goblinframework.dao.mysql.support.UseMysqlClient;
import org.goblinframework.test.runner.GoblinTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.ContextConfiguration;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@RunWith(GoblinTestRunner.class)
@ContextConfiguration("/UT.xml")
public class GoblinStaticPersistenceTest extends SpringManagedBean {

  @Table(table = "MOCK_DATA_T")
  public static class MockData implements Serializable {
    private static final long serialVersionUID = -4568367284300289600L;

    @Id(Id.Generator.AUTO_INC)
    public Long id;
    public String name;
  }

  @Repository
  @UseMysqlClient("_ut")
  public static class MockDataPersistence extends GoblinStaticPersistence<MockData, Long> {
  }

  @Inject private MockDataPersistence persistence;

  @Test
  public void persistence() {
    List<MockData> dataList = new LinkedList<>();
    MockData data = new MockData();
    data.name = RandomUtils.nextObjectId();
    dataList.add(data);
    persistence.$inserts(dataList);
  }
}