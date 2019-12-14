package org.goblinframework.dao.mysql.persistence;

import org.goblinframework.cache.annotation.CacheBean;
import org.goblinframework.cache.annotation.CacheMethod;
import org.goblinframework.cache.annotation.CacheParameter;
import org.goblinframework.cache.bean.GoblinCacheDimension;
import org.goblinframework.cache.core.CacheKeyGenerator;
import org.goblinframework.cache.core.CacheSystem;
import org.goblinframework.cache.module.test.FlushCache;
import org.goblinframework.dao.annotation.PersistenceCacheDimension;
import org.goblinframework.dao.annotation.PersistenceConnection;
import org.goblinframework.dao.ql.Criteria;
import org.goblinframework.dao.ql.Query;
import org.goblinframework.test.runner.GoblinTestRunner;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.ContextConfiguration;

import javax.inject.Inject;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(GoblinTestRunner.class)
@ContextConfiguration("/UT.xml")
@FlushCache(system = CacheSystem.JVM, connection = "_ut")
public class GoblinCacheStaticPersistenceTest {

  @Repository
  @PersistenceConnection(connection = "_ut")
  @MysqlPersistenceTable(table = "MOCK_DATA_T")
  @CacheBean(type = GoblinStaticPersistenceTest.MockData.class, system = CacheSystem.JVM, connection = "_ut")
  @PersistenceCacheDimension(dimension = PersistenceCacheDimension.Dimension.ID_AND_OTHER_FIELDS)
  public static class MockDataPersistence extends GoblinCacheStaticPersistence<GoblinStaticPersistenceTest.MockData, Long> {

    @Override
    protected void calculateCacheDimensions(GoblinStaticPersistenceTest.MockData document, GoblinCacheDimension dimension) {
      dimension.get().add(CacheKeyGenerator.generateCacheKey(GoblinStaticPersistenceTest.MockData.class, document.id));
      dimension.get().add(CacheKeyGenerator.generateCacheKey(GoblinStaticPersistenceTest.MockData.class, "N", document.name));
    }

    @NotNull
    @CacheMethod(GoblinStaticPersistenceTest.MockData.class)
    public List<GoblinStaticPersistenceTest.MockData> findByName(@CacheParameter("N") @NotNull String name) {
      Criteria criteria = Criteria.where("NAME").is(name);
      return __find(Query.query(criteria));
    }
  }

  @Inject private MockDataPersistence persistence;

  @Test
  public void persistence() {
    boolean ret = persistence.exists(1L);
    assertFalse(ret);

    GoblinStaticPersistenceTest.MockData data = new GoblinStaticPersistenceTest.MockData();
    data.id = 1L;
    data.name = "original";
    persistence.insert(data);

    data = persistence.load(1L);
    assertNotNull(data);
    ret = persistence.exists(1L);
    assertTrue(ret);

    assertEquals(1, persistence.findByName("original").size());

    data = new GoblinStaticPersistenceTest.MockData();
    data.id = 1L;
    data.name = "replace";
    ret = persistence.replace(data);
    assertTrue(ret);

    data = persistence.load(1L);
    assertNotNull(data);
    assertEquals("replace", data.name);

    assertEquals(0, persistence.findByName("original").size());
    assertEquals(1, persistence.findByName("replace").size());

    ret = persistence.delete(1L);
    assertTrue(ret);
  }
}