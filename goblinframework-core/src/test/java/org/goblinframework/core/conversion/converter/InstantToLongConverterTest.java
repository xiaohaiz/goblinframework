package org.goblinframework.core.conversion.converter;

import org.goblinframework.core.conversion.ConversionService;
import org.goblinframework.test.runner.GoblinTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;

import java.time.Instant;

import static org.junit.Assert.*;

@RunWith(GoblinTestRunner.class)
@ContextConfiguration("/UT.xml")
public class InstantToLongConverterTest {

  @Test
  public void convert() {
    ConversionService cs = ConversionService.INSTANCE;
    assertTrue(cs.canConvert(Instant.class, Long.class));
    Instant instant = Instant.now();
    Long time = cs.convert(instant, Long.class);
    assertNotNull(time);
    assertEquals(instant.toEpochMilli(), time.longValue());
  }
}