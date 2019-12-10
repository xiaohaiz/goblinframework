<#-- @ftlvariable name="embeddedServerManager" type="org.goblinframework.embedded.server.EmbeddedServerManagerMXBean" -->
<!--suppress HtmlUnknownTarget -->
<html lang="en">
<head>
  <title>EMBEDDED MODULE</title>
</head>
<body>

<h2>EMBEDDED MODULE</h2>

<h3>
  <a class="btn btn-default" href="/index.do" role="button">DASHBOARD</a>
</h3>

<div class="container-fluid">

  <div class="row">
    <table class="table table-bordered table-condensed table-striped">
      <thead>
      <tr class="info">
        <td colspan="3"><strong>Embedded Server(s)</strong></td>
      </tr>
      </thead>
      <tbody>
      <tr>
        <td><strong>upTime</strong></td>
        <td><strong>mode</strong></td>
        <td><strong>name</strong></td>
      </tr>
      <#list embeddedServerManager.embeddedServerList as server>
        <tr>
          <td>${server.upTime!''}</td>
          <td>${server.mode.name()}</td>
          <td>${server.name}</td>
        </tr>
      </#list>
      </tbody>
    </table>
  </div>

</div>

</body>
</html>