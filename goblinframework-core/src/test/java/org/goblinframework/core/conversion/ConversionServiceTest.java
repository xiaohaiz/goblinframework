package org.goblinframework.core.conversion;

import org.goblinframework.core.container.SpringManagedBean;
import org.goblinframework.test.runner.GoblinTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;

import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

@RunWith(GoblinTestRunner.class)
@ContextConfiguration("/UT.xml")
public class ConversionServiceTest extends SpringManagedBean {

  @Test
  public void longToCalendar() {
    ConversionService cs = ConversionService.INSTANCE;
    assertTrue(cs.canConvert(Long.class, Calendar.class));
    long millis = System.currentTimeMillis();
    Calendar calendar = cs.convert(millis, Calendar.class);
    assertNotNull(calendar);
    assertEquals(millis, calendar.getTimeInMillis());
  }

  @Test
  public void longToDate() {
    ConversionService cs = ConversionService.INSTANCE;
    assertTrue(cs.canConvert(Long.class, Date.class));
    long millis = System.currentTimeMillis();
    Date date = cs.convert(millis, Date.class);
    assertNotNull(date);
    assertEquals(millis, date.getTime());
  }

  @Test
  public void longToInstant() {
    ConversionService cs = ConversionService.INSTANCE;
    assertTrue(cs.canConvert(Long.class, Instant.class));
    long millis = System.currentTimeMillis();
    Instant instant = cs.convert(millis, Instant.class);
    assertNotNull(instant);
    assertEquals(millis, instant.toEpochMilli());
  }
}
