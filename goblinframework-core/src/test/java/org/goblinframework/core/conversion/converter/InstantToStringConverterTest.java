package org.goblinframework.core.conversion.converter;

import org.goblinframework.core.conversion.ConversionService;
import org.goblinframework.core.util.DateFormatUtils;
import org.goblinframework.test.runner.GoblinTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;

import java.sql.Date;
import java.time.Instant;

import static org.junit.Assert.*;

@RunWith(GoblinTestRunner.class)
@ContextConfiguration("/UT.xml")
public class InstantToStringConverterTest {

  @Test
  public void convert() {
    ConversionService cs = ConversionService.INSTANCE;
    assertTrue(cs.canConvert(Instant.class, String.class));
    Instant instant = Instant.now();
    String time = cs.convert(instant, String.class);
    assertNotNull(time);
    assertEquals(DateFormatUtils.getDefaultFormatter().format(Date.from(instant)), time);
  }
}