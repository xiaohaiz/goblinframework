package org.goblinframework.core.conversion.converter;

import org.goblinframework.core.conversion.ConversionService;
import org.goblinframework.test.runner.GoblinTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;

import java.util.Calendar;

import static org.junit.Assert.*;

@RunWith(GoblinTestRunner.class)
@ContextConfiguration("/UT.xml")
public class LongToCalendarConverterTest {

  @Test
  public void convert() {
    ConversionService cs = ConversionService.INSTANCE;
    assertTrue(cs.canConvert(Long.class, Calendar.class));
    long time = System.currentTimeMillis();
    Calendar calendar = cs.convert(time, Calendar.class);
    assertNotNull(calendar);
    assertEquals(time, calendar.getTimeInMillis());
  }
}