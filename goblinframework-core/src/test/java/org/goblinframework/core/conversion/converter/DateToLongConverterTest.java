package org.goblinframework.core.conversion.converter;

import org.goblinframework.core.conversion.ConversionService;
import org.goblinframework.test.runner.GoblinTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;

import static org.junit.Assert.*;

@RunWith(GoblinTestRunner.class)
@ContextConfiguration("/UT.xml")
public class DateToLongConverterTest {

  @Test
  public void convert() {
    ConversionService cs = ConversionService.INSTANCE;
    assertTrue(cs.canConvert(Date.class, Long.class));
    Date date = new Date();
    Long time = cs.convert(date, Long.class);
    assertNotNull(time);
    assertEquals(date.getTime(), time.longValue());
  }
}