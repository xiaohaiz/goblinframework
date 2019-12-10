package org.goblinframework.embedded.jetty.server;

import org.goblinframework.core.util.RandomUtils;
import org.goblinframework.embedded.core.handler.ServletHandler;
import org.goblinframework.embedded.core.setting.ServerSetting;
import org.goblinframework.embedded.server.EmbeddedServerMode;
import org.goblinframework.test.runner.GoblinTestRunner;
import org.goblinframework.webmvc.servlet.ServletRequest;
import org.goblinframework.webmvc.servlet.ServletResponse;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;

import javax.servlet.ServletException;

@RunWith(GoblinTestRunner.class)
@ContextConfiguration("/UT.xml")
public class JettyEmbeddedServerTest {

  @Test
  public void server() {
    String name = RandomUtils.nextObjectId();
    ServerSetting setting = ServerSetting.builder()
        .name(name)
        .mode(EmbeddedServerMode.JETTY)
        .applyHandlerSetting(handlerSettingBuilder -> {
          handlerSettingBuilder.contextPath("/a");
          handlerSettingBuilder.servletHandler(new ServletHandler() {
            @NotNull
            @Override
            public String transformLookupPath(@NotNull String path) {
              return path;
            }

            @Override
            public void handle(@NotNull ServletRequest request, @NotNull ServletResponse response) throws ServletException {
            }
          });
        })
        .nextHandlerSetting()
        .applyHandlerSetting(handlerSettingBuilder -> {
          handlerSettingBuilder.contextPath("/b");
          handlerSettingBuilder.servletHandler(new ServletHandler() {
            @NotNull
            @Override
            public String transformLookupPath(@NotNull String path) {
              return path;
            }

            @Override
            public void handle(@NotNull ServletRequest request, @NotNull ServletResponse response) throws ServletException {
            }
          });
        })
        .build();

    JettyEmbeddedServer server = new JettyEmbeddedServer(setting);
    server.start();
    server.stop();
  }
}
