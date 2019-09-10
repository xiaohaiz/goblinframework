<#-- @ftlvariable name="RequestHandlerManagerBuilderMXBean" type="org.goblinframework.webmvc.handler.RequestHandlerManagerBuilderMXBean" -->
<!--suppress HtmlUnknownTarget -->
<html>
<head>
  <title>WEBMVC MODULE</title>
</head>
<body>

<h2>WEBMVC MODULE</h2>

<h3>
  <a class="btn btn-default" href="/index.do" role="button">DASHBOARD</a>
</h3>

<div class="container-fluid">

    <#if RequestHandlerManagerBuilderMXBean.requestHandlerManagerList?has_content>
      <div class="row">
        <table class="table table-bordered table-condensed table-striped">
          <thead>
          <tr class="info">
            <td colspan="1"><strong>Request handler manager(s)</strong></td>
          </tr>
          </thead>
          <tbody>
          <tr>
            <td><strong>name</strong></td>
          </tr>
          <#list RequestHandlerManagerBuilderMXBean.requestHandlerManagerList as m>
            <tr>
              <td>${m.name}</td>
            </tr>
          </#list>
          </tbody>
        </table>
      </div>
    </#if>

</div>

</body>
</html>