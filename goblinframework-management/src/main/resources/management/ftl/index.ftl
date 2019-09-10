<#-- @ftlvariable name="managementModules" type="java.util.List<com.github.goblin.management.controller.ManagementModule>" -->
<!--suppress HtmlUnknownTarget -->
<html>
<head>
  <title>Goblin Internal Management System</title>
</head>
<body>

<h2>Goblin Internal Management System</h2>

<div class="container-fluid">

<#if managementModules?has_content>
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
          <#list managementModules as module>
            <a class="btn btn-default"
               href="${module.managementURL}"
               role="button">${module.moduleName.name()}</a>
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