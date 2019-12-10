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
        <td colspan="1"><strong>Embedded Server(s)</strong></td>
      </tr>
      </thead>
      <tbody>
      <tr>
        <td><strong>upTime</strong></td>
      </tr>
      <#list embeddedServerManager.embeddedServerList as server>
        <tr>
          <td>${server.upTime!''}</td>
        </tr>
      </#list>
      </tbody>
    </table>
  </div>

</div>

</body>
</html>