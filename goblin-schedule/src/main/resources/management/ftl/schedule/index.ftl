<#-- @ftlvariable name="cronTaskManagerMXBean" type="org.goblinframework.schedule.cron.CronTaskManagerMXBean" -->
<!--suppress HtmlUnknownTarget -->
<html lang="zh">
<head>
  <title>SCHEDULE MODULE</title>
</head>
<body>

<h2>SCHEDULE MODULE</h2>

<h3>
  <a class="btn btn-default" href="/index.do" role="button">DASHBOARD</a>
</h3>

<div class="container-fluid">

    <#if cronTaskManagerMXBean.cronTaskList?has_content>
      <div class="row">
        <table class="table table-bordered table-condensed table-striped">
          <thead>
          <tr class="info">
            <td colspan="5"><strong>Cron task(s)</strong></td>
          </tr>
          </thead>
          <tbody>
          <tr>
            <td><strong>upTime</strong></td>
            <td><strong>name</strong></td>
            <td><strong>cronExpression</strong></td>
            <td><strong>concurrent</strong></td>
            <td><strong>executeTimes</strong></td>
          </tr>
          <#list cronTaskManagerMXBean.cronTaskList as t>
            <tr>
              <td>${t.upTime}</td>
              <td>${t.name}</td>
              <td>${t.cronExpression}</td>
              <td>${t.getConcurrent()?c}</td>
              <td>${t.executeTimes}</td>
            </tr>
          </#list>
          </tbody>
        </table>
      </div>
    </#if>

</div>

</body>
</html>