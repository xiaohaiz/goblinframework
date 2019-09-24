<#-- @ftlvariable name="configLoaderMXBean" type="org.goblinframework.core.config.ConfigLoaderMXBean" -->
<!--suppress HtmlUnknownTarget -->
<html>
<head>
  <title>CONFIG MODULE</title>
</head>
<body>

<h2>CONFIG MODULE</h2>

<h3>
  <a class="btn btn-default" href="/index.do" role="button">DASHBOARD</a>
</h3>

<div class="container-fluid">

  <div class="row">
    <table class="table table-bordered table-condensed table-striped">
      <thead>
      <tr class="info">
        <td colspan="2"><strong>Config location scanner</strong></td>
      </tr>
      </thead>
      <tbody>
      <#assign cls=configLoaderMXBean.configLocationScanner>
      <tr>
        <td><strong>configPath</strong></td>
        <td>${cls.configPath!''}</td>
      </tr>
      <tr>
        <td><strong>foundInFileSystem</strong></td>
        <td>${cls.getFoundInFileSystem()?c}</td>
      </tr>
      <tr>
        <td><strong>candidatePathList</strong></td>
        <td>
            <#if cls.candidatePathList?has_content>
                <#list cls.candidatePathList as c>
                  <li>${c}</li>
                </#list>
            </#if>
        </td>
      </tr>
      </tbody>
    </table>
  </div>

</div>

</body>
</html>