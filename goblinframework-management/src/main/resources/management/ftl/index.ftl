<#-- @ftlvariable name="modules" type="java.util.List<org.goblinframework.api.system.IModule>" -->
<#-- @ftlvariable name="subModules" type="java.util.List<org.goblinframework.api.system.ISubModule>" -->
<#-- @ftlvariable name="extModules" type="java.util.List<org.goblinframework.api.system.IExtModule>" -->
<!--suppress HtmlUnknownTarget -->
<html lang="zh">
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
                     role="button">${module.id().name()}</a>
                </#list>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </#if>

    <#if subModules?has_content>
      <div class="row">
        <table class="table table-bordered table-condensed table-striped">
          <thead>
          <tr class="info">
            <td><strong>Installed Sub-Module(s)</strong></td>
          </tr>
          </thead>
          <tbody>
          <tr>
            <td>
                <#list subModules as module>
                  <a class="btn btn-default"
                     href="${module.managementEntrance()}"
                     role="button">${module.id().fullName()}</a>
                </#list>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </#if>

    <#if extModules?has_content>
      <div class="row">
        <table class="table table-bordered table-condensed table-striped">
          <thead>
          <tr class="info">
            <td><strong>Installed Ext-Module(s)</strong></td>
          </tr>
          </thead>
          <tbody>
          <tr>
            <td>
                <#list extModules as module>
                  <a class="btn btn-default"
                     href="${module.managementEntrance()}"
                     role="button">${module.id()}</a>
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