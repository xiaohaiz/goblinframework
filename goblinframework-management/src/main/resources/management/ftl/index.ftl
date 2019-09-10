<#-- @ftlvariable name="modules" type="java.util.List<org.goblinframework.core.bootstrap.GoblinModule>" -->
<!--suppress HtmlUnknownTarget -->
<html>
<head>
  <title>Goblin Internal Management System</title>
</head>
<body>

<h2>Goblin Internal Management System</h2>

<div class="container-fluid">

    <#if modules?has_content>
      <div class="row">
        <table class="table table-bordered table-condensed table-striped">
          <thead>
          <tr class="info">
            <td><strong>Installed Module(s)</strong></td>
          </tr>
          </thead>
          <tbody>
          <tr>
            <td>
                <#list modules as module>
                  <a class="btn btn-default"
                     href="${module.managementEntrance()}"
                     role="button">${module.name()}</a>
                </#list>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </#if>

</div>

</body>
</html>