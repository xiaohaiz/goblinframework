package org.eclipse.jetty.server;

import org.eclipse.jetty.io.Connection;
import org.eclipse.jetty.io.EndPoint;
import org.goblinframework.api.annotation.Compatible;

@Compatible(
    group = "org.eclipse.jetty",
    artifact = "jetty-server",
    version = "9.4.24.v20191120")
public class GoblinHttpConnectionFactory extends HttpConnectionFactory {

  @Override
  public Connection newConnection(Connector connector, EndPoint endPoint) {
    HttpConnection conn = new GoblinHttpConnection(getHttpConfiguration(), connector, endPoint, getHttpCompliance(), isRecordHttpComplianceViolations());
    return configure(conn, connector, endPoint);
  }
}
