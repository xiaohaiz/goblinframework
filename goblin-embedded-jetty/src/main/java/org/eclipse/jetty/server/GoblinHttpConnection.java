package org.eclipse.jetty.server;

import org.eclipse.jetty.http.HttpCompliance;
import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.io.EndPoint;
import org.goblinframework.api.annotation.Compatible;
import org.goblinframework.core.util.HttpUtils;

@Compatible(
    group = "org.eclipse.jetty",
    artifact = "jetty-server",
    version = "9.4.24.v20191120")
public class GoblinHttpConnection extends HttpConnection {

  GoblinHttpConnection(HttpConfiguration config, Connector connector, EndPoint endPoint, HttpCompliance compliance, boolean recordComplianceViolations) {
    super(config, connector, endPoint, compliance, recordComplianceViolations);
  }

  @Override
  protected HttpChannelOverHttp newHttpChannel() {
    return new HttpChannelOverHttp(this, getConnector(), getHttpConfiguration(), getEndPoint(), this) {
      @Override
      public boolean startRequest(String method, String uri, HttpVersion version) {
        return super.startRequest(method, HttpUtils.compactContinuousSlashes(uri, false), version);
      }
    };
  }
}
