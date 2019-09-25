package org.goblinframework.dao.mysql.persistence;

import org.goblinframework.api.cache.*;
import org.goblinframework.api.dao.GoblinCacheDimension;
import org.goblinframework.api.dao.GoblinDatabaseConnection;
import org.goblinframework.cache.core.support.CacheDimension;
import org.goblinframework.cache.core.util.CacheKeyGenerator;
import org.goblinframework.dao.core.cql.Criteria;
import org.goblinframework.dao.core.cql.Query;
import org.goblinframework.dao.mysql.module.test.RebuildMysqlTable;
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
@FlushCache(system = CacheSystem.JVM)
public class GoblinCachedStaticPersistenceTest {

  @Repository
  @GoblinDatabaseConnection(name = "_ut")
  @GoblinCacheBean(type = GoblinStaticPersistenceTest.MockData.class, system = CacheSystem.JVM, name = "JVM")
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