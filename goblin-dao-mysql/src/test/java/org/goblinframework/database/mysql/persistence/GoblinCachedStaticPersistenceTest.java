package org.goblinframework.database.mysql.persistence;

import org.goblinframework.cache.core.annotation.FlushCache;
import org.goblinframework.cache.core.annotation.GoblinCacheBean;
import org.goblinframework.cache.core.annotation.GoblinCacheMethod;
import org.goblinframework.cache.core.annotation.GoblinCacheParameter;
import org.goblinframework.cache.core.cache.CacheSystem;
import org.goblinframework.cache.core.support.CacheDimension;
import org.goblinframework.cache.core.util.CacheKeyGenerator;
import org.goblinframework.database.core.annotation.GoblinCacheDimension;
import org.goblinframework.database.core.annotation.GoblinDatabaseConnection;
import org.goblinframework.database.core.eql.Criteria;
import org.goblinframework.database.core.eql.Query;
import org.goblinframework.database.mysql.module.test.RebuildMysqlTable;
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
@FlushCache(system = CacheSystem.JVM, name = "_ut")
public class GoblinCachedStaticPersistenceTest {

  @Repository
  @GoblinDatabaseConnection(name = "_ut")
  @GoblinCacheBean(type = GoblinStaticPersistenceTest.MockData.class, system = CacheSystem.JVM, name = "_ut")
  @GoblinCacheDimension(dimension = GoblinCacheDimension.Dimension.ID_AND_OTHER_FIELDS)
  public static class MockDataPersistence extends GoblinCachedStaticPersistence<GoblinStaticPersistenceTest.MockData, Long> {

    @Override
    protected void calculateCacheDimensions(GoblinStaticPersistenceTest.MockData document, CacheDimension dimension) {
      dimension.get().add(CacheKeyGenerator.generateCacheKey(GoblinStaticPersistenceTest.MockData.class, document.id));
      dimension.get().add(CacheKeyGenerator.generateCacheKey(GoblinStaticPersistenceTest.MockData.class, "N", document.name));
    }

    @NotNull
    @GoblinCacheMethod(GoblinStaticPersistenceTest.MockData.class)
    public List<GoblinStaticPersistenceTest.MockData> findByName(@GoblinCacheParameter("N") @NotNull String name) {
      Criteria criteria = Criteria.where("NAME").is(name);
      return directQuery(Query.query(criteria));
    }
  }

  @Inject private MockDataPersistence persistence;

  @Test
  @RebuildMysqlTable(name = "_ut", entity = GoblinStaticPersistenceTest.MockData.class)
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