package org.goblinframework.core.conversion.converter;

import org.goblinframework.core.conversion.ConversionService;
import org.goblinframework.test.runner.GoblinTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;

import java.time.Instant;
import java.util.Date;

import static org.junit.Assert.*;

@RunWith(GoblinTestRunner.class)
@ContextConfiguration("/UT.xml")
public class InstantToDateConverterTest {

  @Test
  public void convert() {
    ConversionService cs = ConversionService.INSTANCE;
    assertTrue(cs.canConvert(Instant.class, Date.class));
    Instant instant = Instant.now();
    Date date = cs.convert(instant, Date.class);
    assertNotNull(date);
    assertEquals(instant.toEpochMilli(), date.getTime());
  }
}