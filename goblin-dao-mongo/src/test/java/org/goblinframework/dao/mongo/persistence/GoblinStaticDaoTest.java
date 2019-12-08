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

@RunWith(GoblinTestRunner.class)
@ContextConfiguration("/UT.xml")
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
  @DropMongoDatabase("_ut")
  public void insert() {
    User user = new User();
    user.userId = 1L;
    userDao.insert(user);
  }
}
