package org.goblinframework.dao.mongo.persistence;

import org.goblinframework.api.dao.CreateTime;
import org.goblinframework.api.dao.Id;
import org.goblinframework.api.dao.Revision;
import org.goblinframework.api.dao.UpdateTime;
import org.goblinframework.cache.annotation.CacheBean;
import org.goblinframework.cache.annotation.CacheMethod;
import org.goblinframework.cache.annotation.CacheParameter;
import org.goblinframework.cache.bean.GoblinCacheDimension;
import org.goblinframework.cache.core.CacheKeyGenerator;
import org.goblinframework.cache.core.CacheSystem;
import org.goblinframework.cache.module.test.FlushCache;
import org.goblinframework.dao.annotation.PersistenceCacheDimension;
import org.goblinframework.dao.annotation.PersistenceConnection;
import org.goblinframework.dao.mongo.module.test.DropDatabase;
import org.goblinframework.dao.ql.Criteria;
import org.goblinframework.dao.ql.Query;
import org.goblinframework.test.runner.GoblinTestRunner;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.ContextConfiguration;

import javax.inject.Inject;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(GoblinTestRunner.class)
@ContextConfiguration("/UT.xml")
@DropDatabase(connection = "_ut")
@FlushCache(system = CacheSystem.JVM, connection = "_ut")
public class GoblinCacheStaticDaoTest {

  public static class User implements Serializable {
    private static final long serialVersionUID = -5245270554043361654L;

    @Id(Id.Generator.NONE)
    public Long userId;
    @CreateTime
    public Instant createTime;
    @UpdateTime
    public Instant updateTime;
    @Revision
    public Integer revision;

    public String userName;
  }

  @Repository
  @PersistenceConnection(connection = "_ut")
  @MongoPersistenceDatabase(database = "test")
  @MongoPersistenceCollection(collection = "user")
  @CacheBean(type = User.class, system = CacheSystem.JVM, connection = "_ut", wrapper = true)
  @PersistenceCacheDimension(dimension = PersistenceCacheDimension.Dimension.ID_AND_OTHER_FIELDS)
  public static class UserDao extends GoblinCacheStaticDao<User, Long> {

    @Override
    protected void calculateCacheDimensions(User document, GoblinCacheDimension dimension) {
      dimension.get().add(CacheKeyGenerator.generateCacheKey(User.class, document.userId));
      dimension.get().add(CacheKeyGenerator.generateCacheKey(User.class, "userName", document.userName));
    }

    @CacheMethod(User.class)
    public List<User> findByUserName(@NotNull @CacheParameter("userName") String userName) {
      Criteria criteria = Criteria.where("userName").is(userName);
      return __find(Query.query(criteria));
    }
  }

  @Inject private UserDao userDao;


  @Test
  public void findByUserName() {
    List<User> userList = userDao.findByUserName("A");
    assertEquals(0, userList.size());
    User user = new User();
    user.userId = 1L;
    user.userName = "A";
    userDao.insert(user);
    userList = userDao.findByUserName("A");
    assertEquals(1, userList.size());
  }

  @Test
  public void load() {
    Long userId = 1L;
    User user = userDao.load(userId);
    assertNull(user);
    user = new User();
    user.userId = userId;
    user.userName = "Naomi";
    userDao.insert(user);
    user = userDao.load(userId);
    assertNotNull(user);
  }

  @Test
  public void loads() {
    List<Long> userIds = Arrays.asList(1L, 2L, 3L);
    Map<Long, User> users = userDao.loads(userIds);
    assertTrue(users.isEmpty());

    List<User> userList = new ArrayList<>();
    User user = new User();
    user.userId = 1L;
    userList.add(user);
    user = new User();
    user.userId = 3L;
    userList.add(user);
    userDao.inserts(userList);

    users = userDao.loads(userIds);
    assertEquals(2, users.size());
    assertNotNull(users.get(1L));
    assertNotNull(users.get(3L));
  }

  @Test
  public void exists() {
    assertFalse(userDao.exists(1L));
    User user = new User();
    user.userId = 1L;
    userDao.insert(user);
    assertTrue(userDao.exists(1L));
    userDao.load(1L);
    assertTrue(userDao.exists(1L));
  }

  @Test
  public void replace() {
    User user = new User();
    user.userId = 1L;
    user.userName = "A";
    userDao.insert(user);
    user = userDao.load(1L);
    assertNotNull(user);
    assertEquals("A", user.userName);
    user = new User();
    user.userId = 1L;
    user.userName = "B";
    user = userDao.replace(user);
    assertNotNull(user);
    user = userDao.load(1L);
    assertNotNull(user);
    assertEquals("B", user.userName);
  }
}