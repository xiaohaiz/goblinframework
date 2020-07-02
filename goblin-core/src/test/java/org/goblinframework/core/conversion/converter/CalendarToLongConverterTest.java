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
public class CalendarToLongConverterTest {

  @Test
  public void convert() {
    ConversionService cs = ConversionService.INSTANCE;
    assertTrue(cs.canConvert(Calendar.class, Long.class));
    Calendar calendar = Calendar.getInstance();
    Long time = cs.convert(calendar, Long.class);
    assertNotNull(time);
    assertEquals(calendar.getTimeInMillis(), time.longValue());
  }
}