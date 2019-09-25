<#-- @ftlvariable name="remoteServerManagerMXBean" type="org.goblinframework.remote.server.handler.RemoteServerManagerMXBean" -->
<!--suppress HtmlUnknownTarget -->
<html lang="zh">
<head>
  <title>EVENT MODULE</title>
</head>
<body>

<h2>EVENT MODULE</h2>

<h3>
  <a class="btn btn-default" href="/index.do" role="button">DASHBOARD</a>
</h3>

<div class="container-fluid">

    <#if remoteServerManagerMXBean.remoteServer??>
        <#assign rs=remoteServerManagerMXBean.remoteServer>
      <div class="row">
        <table class="table table-bordered table-condensed table-striped">
          <thead>
          <tr class="info">
            <td colspan="5"><strong>Remote server</strong></td>
          </tr>
          </thead>
          <tbody>
          <tr>
            <td><strong>upTime</strong></td>
            <td><strong>name</strong></td>
            <td><strong>running</strong></td>
            <td><strong>host</strong></td>
            <td><strong>port</strong></td>
          </tr>
          <#assign ts=rs.transportServer>
          <tr>
            <td>${ts.upTime}</td>
            <td>${ts.name}</td>
            <td>${ts.getRunning()?c}</td>
            <td>${ts.host!''}</td>
            <td>${ts.port}</td>
          </tr>
          </tbody>
        </table>
      </div>
    </#if>

</div>

</body>
</html>