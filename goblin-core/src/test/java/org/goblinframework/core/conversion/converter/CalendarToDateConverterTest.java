package org.goblinframework.core.conversion.converter;

import org.goblinframework.core.conversion.ConversionService;
import org.goblinframework.test.runner.GoblinTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

@RunWith(GoblinTestRunner.class)
@ContextConfiguration("/UT.xml")
public class CalendarToDateConverterTest {

  @Test
  public void convert() {
    ConversionService cs = ConversionService.INSTANCE;
    assertTrue(cs.canConvert(Calendar.class, Date.class));
    Calendar calendar = Calendar.getInstance();
    Date date = cs.convert(calendar, Date.class);
    assertNotNull(date);
    assertEquals(calendar.getTimeInMillis(), date.getTime());
  }
}