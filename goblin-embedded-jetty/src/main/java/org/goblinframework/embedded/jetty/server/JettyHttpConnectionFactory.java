package org.goblinframework.embedded.jetty.server;

import org.eclipse.jetty.io.Connection;
import org.eclipse.jetty.io.EndPoint;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConnection;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.goblinframework.api.annotation.Compatible;

@Compatible(
    group = "org.eclipse.jetty",
    artifact = "jetty-server",
    version = "9.4.24.v20191120")
public class JettyHttpConnectionFactory extends HttpConnectionFactory {

  @Override
  public Connection newConnection(Connector connector, EndPoint endPoint) {
    HttpConnection conn = new JettyHttpConnection(getHttpConfiguration(), connector, endPoint, getHttpCompliance(), isRecordHttpComplianceViolations());
    return configure(conn, connector, endPoint);
  }
}
