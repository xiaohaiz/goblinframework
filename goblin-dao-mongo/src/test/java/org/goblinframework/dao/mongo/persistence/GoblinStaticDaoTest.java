package org.goblinframework.dao.mongo.persistence;

import org.goblinframework.api.dao.Id;
import org.goblinframework.dao.annotation.PersistenceConnection;
import org.goblinframework.dao.mongo.annotation.MongoPersistenceCollection;
import org.goblinframework.dao.mongo.annotation.MongoPersistenceDatabase;
import org.goblinframework.database.mongo.module.test.DropMongoDatabase;
import org.goblinframework.test.runner.GoblinTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.ContextConfiguration;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(GoblinTestRunner.class)
@ContextConfiguration("/UT.xml")
@DropMongoDatabase("_ut")
public class GoblinStaticDaoTest {

  public static class User implements Serializable {
    private static final long serialVersionUID = 4591273205433269031L;

    @Id(Id.Generator.NONE)
    public Long userId;
  }

  @Repository
  @PersistenceConnection(connection = "_ut")
  @MongoPersistenceDatabase(database = "test")
  @MongoPersistenceCollection(collection = "user")
  public static class UserDao extends GoblinStaticDao<User, Long> {
  }

  @Inject private UserDao userDao;

  @Test
  public void insert() {
    User user = new User();
    user.userId = 1L;
    userDao.insert(user);
  }

  @Test
  public void inserts() {
    List<User> users = new ArrayList<>();
    User user = new User();
    user.userId = 1L;
    users.add(user);
    user = new User();
    user.userId = 2L;
    users.add(user);
    user = new User();
    user.userId = 3L;
    users.add(user);
    userDao.inserts(users);
  }

  @Test
  public void load() {
    User user = userDao.load(1L);
    assertNull(user);
    user = new User();
    user.userId = 1L;
    userDao.insert(user);
    user = userDao.load(1L);
    assertNotNull(user);
  }

  @Test
  public void loads() {
    List<User> users = new ArrayList<>();
    User user = new User();
    user.userId = 1L;
    users.add(user);
    user = new User();
    user.userId = 2L;
    users.add(user);
    user = new User();
    user.userId = 3L;
    users.add(user);
    userDao.inserts(users);

    List<Long> ids = Arrays.asList(1L, 2L, 3L);
    Map<Long, User> map = userDao.loads(ids);
    assertEquals(3, map.size());
  }

  @Test
  public void exists() {
    assertFalse(userDao.exists(1L));
    User user = new User();
    user.userId = 1L;
    userDao.insert(user);
    assertTrue(userDao.exists(1L));
  }

  @Test
  public void count() {
    List<User> users = new ArrayList<>();
    User user = new User();
    user.userId = 1L;
    users.add(user);
    user = new User();
    user.userId = 2L;
    users.add(user);
    user = new User();
    user.userId = 3L;
    users.add(user);
    userDao.inserts(users);
    assertEquals(3, userDao.count());
    assertEquals(3, userDao.find().size());
  }
}
