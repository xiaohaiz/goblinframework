package org.goblinframework.core.conversion.converter;

import org.goblinframework.core.conversion.ConversionService;
import org.goblinframework.core.util.DateFormatUtils;
import org.goblinframework.test.runner.GoblinTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;

import java.util.Calendar;

import static org.junit.Assert.*;

@RunWith(GoblinTestRunner.class)
@ContextConfiguration("/UT.xml")
public class CalendarToStringConverterTest {

  @Test
  public void convert() {
    ConversionService cs = ConversionService.INSTANCE;
    assertTrue(cs.canConvert(Calendar.class, String.class));
    Calendar calendar = Calendar.getInstance();
    String time = cs.convert(calendar, String.class);
    assertNotNull(time);
    assertEquals(DateFormatUtils.getDefaultFormatter().format(calendar.getTime()), time);
  }
}