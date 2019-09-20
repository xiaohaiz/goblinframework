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
public class CalendarToInstantConverterTest {

  @Test
  public void convert() {
    ConversionService cs = ConversionService.INSTANCE;
    assertTrue(cs.canConvert(Calendar.class, Instant.class));
    Calendar calendar = Calendar.getInstance();
    Instant instant = cs.convert(calendar, Instant.class);
    assertNotNull(instant);
    assertEquals(calendar.getTimeInMillis(), instant.toEpochMilli());
  }
}