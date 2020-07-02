<#-- @ftlvariable name="transportServerManager" type="org.goblinframework.transport.server.channel.TransportServerManagerMXBean" -->
<!--suppress HtmlUnknownTarget -->
<html lang="en">
<head>
  <title>TRANSPORT SERVER MODULE</title>
</head>
<body>

<h2>TRANSPORT SERVER MODULE</h2>

<h3>
  <a class="btn btn-default" href="/index.do" role="button">DASHBOARD</a>
</h3>

<div class="container-fluid">

  <div class="row">
    <table class="table table-bordered table-condensed table-striped">
      <thead>
      <tr class="info">
        <td colspan="5"><strong>Transport server(s)</strong></td>
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
      <#list transportServerManager.transportServerList as server>
        <tr>
          <td>${server.upTime!''}</td>
          <td>${server.name}</td>
          <td>${server.getRunning()?c}</td>
          <td>${server.host!''}</td>
          <td>${server.port!''}</td>
        </tr>
      </#list>
      </tbody>
    </table>
  </div>

</div>

</body>
</html>