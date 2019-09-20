package org.goblinframework.core.conversion.converter;

import org.goblinframework.core.conversion.ConversionService;
import org.goblinframework.test.runner.GoblinTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;

import java.time.Instant;
import java.util.Calendar;

import static org.junit.Assert.*;

@RunWith(GoblinTestRunner.class)
@ContextConfiguration("/UT.xml")
public class InstantToCalendarConverterTest {

  @Test
  public void convert() {
    ConversionService cs = ConversionService.INSTANCE;
    assertTrue(cs.canConvert(Instant.class, Calendar.class));
    Instant instant = Instant.now();
    Calendar calendar = cs.convert(instant, Calendar.class);
    assertNotNull(calendar);
    assertEquals(instant.toEpochMilli(), calendar.getTimeInMillis());
  }
}