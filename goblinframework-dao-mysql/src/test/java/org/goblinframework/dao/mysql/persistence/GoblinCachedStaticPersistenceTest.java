package org.goblinframework.dao.mysql.persistence;

import org.goblinframework.cache.core.annotation.CacheSystem;
import org.goblinframework.cache.core.annotation.GoblinCacheBean;
import org.goblinframework.cache.core.module.test.FlushInJvmCache;
import org.goblinframework.cache.core.support.GoblinCacheDimension;
import org.goblinframework.cache.core.util.CacheKeyGenerator;
import org.goblinframework.dao.mysql.module.test.RebuildMysqlTable;
import org.goblinframework.dao.mysql.support.UseMysqlClient;
import org.goblinframework.test.runner.GoblinTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.ContextConfiguration;

import javax.inject.Inject;

import static org.junit.Assert.*;

@RunWith(GoblinTestRunner.class)
@ContextConfiguration("/UT.xml")
@FlushInJvmCache
public class GoblinCachedStaticPersistenceTest {

  @Repository
  @UseMysqlClient("_ut")
  @GoblinCacheBean(type = GoblinStaticPersistenceTest.MockData.class, system = CacheSystem.JVM, name = "JVM")
  @org.goblinframework.cache.core.annotation.GoblinCacheDimension(org.goblinframework.cache.core.annotation.GoblinCacheDimension.Distribution.ID_FIELD)
  public static class MockDataPersistence extends GoblinCachedStaticPersistence<GoblinStaticPersistenceTest.MockData, Long> {

    @Override
    protected void calculateCacheDimensions(GoblinStaticPersistenceTest.MockData document, GoblinCacheDimension dimension) {
      dimension.get().add(CacheKeyGenerator.generateCacheKey(GoblinStaticPersistenceTest.MockData.class, document.id));
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
    persistence.insert(data);

    data = persistence.load(1L);
    assertNotNull(data);
    ret = persistence.exists(1L);
    assertTrue(ret);
  }
}