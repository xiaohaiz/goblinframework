package org.goblinframework.dao.mysql.persistence;

import org.goblinframework.api.dao.GoblinField;
import org.goblinframework.api.dao.GoblinId;
import org.goblinframework.api.dao.GoblinRevision;
import org.goblinframework.core.util.StringUtils;
import org.goblinframework.dao.core.annotation.GoblinConnection;
import org.goblinframework.dao.mysql.annotation.GoblinTable;
import org.goblinframework.database.mysql.module.test.RebuildMysqlTable;
import org.goblinframework.test.runner.GoblinTestRunner;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.ContextConfiguration;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@RunWith(GoblinTestRunner.class)
@ContextConfiguration("/UT.xml")
public class GoblinDynamicPersistenceTest {

  public static class UserLoginRecord implements Serializable {
    private static final long serialVersionUID = -4970313634722267478L;

    @GoblinId(GoblinId.Generator.NONE)
    @GoblinField("USER_ID")
    public Long userId;
    @GoblinField("LOGIN_TIME")
    public Date loginTime;
    @GoblinRevision
    @GoblinField("REVISION")
    public Integer revision;
  }

  @Repository
  @GoblinConnection(name = "_ut")
  @GoblinTable(table = "UT_USER_LOGIN_RECORD_{}", dynamic = true)
  public static class UserLoginRecordPersistence extends GoblinDynamicPersistence<UserLoginRecord, Long> {
    @NotNull
    @Override
    protected String calculateTableName(@NotNull String template, @NotNull UserLoginRecord document) {
      Long userId = document.userId;
      return StringUtils.formatMessage(template, userId % 10);
    }
  }

  @Inject
  private UserLoginRecordPersistence userLoginRecordPersistence;

  @Test
  @RebuildMysqlTable(
      name = "_ut",
      table = "UT_USER_LOGIN_RECORD_{}",
      to = 9
  )
  public void userLoginRecordPersistence() {
    List<UserLoginRecord> recordList = new ArrayList<>();
    for (long userId = 1; userId <= 100; userId++) {
      UserLoginRecord record = new UserLoginRecord();
      record.userId = userId;
      record.loginTime = new Date();
      recordList.add(record);
    }
    userLoginRecordPersistence.inserts(recordList);

    List<Long> userIds = recordList.stream().map(e -> e.userId).collect(Collectors.toList());
    Map<Long, UserLoginRecord> records = userLoginRecordPersistence.loads(userIds);
    assertEquals(100, records.size());
  }


  @Test
  @RebuildMysqlTable(name = "_ut", table = "UT_USER_LOGIN_RECORD_{}")
  public void exists() {
    Long userId = 10L;
    assertFalse(userLoginRecordPersistence.exists(userId));
    UserLoginRecord record = new UserLoginRecord();
    record.userId = userId;
    record.loginTime = new Date();
    userLoginRecordPersistence.insert(record);
    assertTrue(userLoginRecordPersistence.exists(userId));
  }
}