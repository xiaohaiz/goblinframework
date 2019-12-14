package org.goblinframework.dao.mongo.persistence;

import org.goblinframework.api.dao.CreateTime;
import org.goblinframework.api.dao.Id;
import org.goblinframework.api.dao.Revision;
import org.goblinframework.api.dao.UpdateTime;
import org.goblinframework.cache.annotation.CacheBean;
import org.goblinframework.cache.bean.GoblinCacheDimension;
import org.goblinframework.cache.core.CacheKeyGenerator;
import org.goblinframework.cache.core.CacheSystem;
import org.goblinframework.cache.module.test.FlushCache;
import org.goblinframework.dao.annotation.PersistenceCacheDimension;
import org.goblinframework.dao.annotation.PersistenceConnection;
import org.goblinframework.dao.mongo.module.test.DropDatabase;
import org.goblinframework.test.runner.GoblinTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.ContextConfiguration;

import javax.inject.Inject;
import java.io.Serializable;
import java.time.Instant;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
  @PersistenceCacheDimension(dimension = PersistenceCacheDimension.Dimension.ID_FIELD)
  public static class UserDao extends GoblinCacheStaticDao<User, Long> {

    @Override
    protected void calculateCacheDimensions(User document, GoblinCacheDimension dimension) {
      dimension.get().add(CacheKeyGenerator.generateCacheKey(User.class, document.userId));
    }
  }

  @Inject private UserDao userDao;

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
}