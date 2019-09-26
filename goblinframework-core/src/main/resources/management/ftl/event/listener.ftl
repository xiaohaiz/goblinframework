<#-- @ftlvariable name="workerId" type="java.lang.String" -->
<#-- @ftlvariable name="eventListenerMXBean" type="org.goblinframework.core.event.GoblinEventListenerMXBean" -->
<!--suppress HtmlUnknownTarget -->
<html lang="zh">
<head>
  <title>EVENT MODULE</title>
</head>
<body>

<h2>EVENT MODULE</h2>

<h3>
  <a class="btn btn-default" href="/index.do" role="button">DASHBOARD</a>
  <a class="btn btn-default" href="/goblin/event/index.do" role="button">EVENT MODULE</a>
  <a class="btn btn-default" href="/goblin/event/worker.do?workerId=${workerId}" role="button">EVENT BUS WORKER</a>
</h3>

<div class="container-fluid">

    <#if eventListenerMXBean??>
        <#assign listener=eventListenerMXBean>

      <div class="row">
        <table class="table table-bordered table-condensed table-striped">
          <thead>
          <tr class="info">
            <td colspan="6"><strong>Event listener</strong></td>
          </tr>
          </thead>
          <tbody>
          <tr>
            <td><strong>upTime</strong></td>
            <td><strong>listener</strong></td>
            <td><strong>accepted</strong></td>
            <td><strong>rejected</strong></td>
            <td><strong>succeed</strong></td>
            <td><strong>failed</strong></td>
          </tr>
          <tr>
            <td>${listener.upTime}</td>
            <td>${listener.listener}</td>
            <td>${listener.acceptedCount}</td>
            <td>${listener.rejectedCount}</td>
            <td>${listener.succeedCount}</td>
            <td>${listener.failedCount}</td>
          </tr>
          </tbody>
        </table>
      </div>

        <#if listener.eventCountList?has_content>
          <div class="row">
            <table class="table table-bordered table-condensed table-striped">
              <thead>
              <tr class="info">
                <td colspan="5"><strong>Event count(s)</strong></td>
              </tr>
              </thead>
              <tbody>
              <tr>
                <td><strong>event</strong></td>
                <td><strong>accepted</strong></td>
                <td><strong>rejected</strong></td>
                <td><strong>succeed</strong></td>
                <td><strong>failed</strong></td>
              </tr>
              <#list listener.eventCountList as count>
                <tr>
                  <td>${count.event}</td>
                  <td>${count.acceptedCount}</td>
                  <td>${count.rejectedCount}</td>
                  <td>${count.succeedCount}</td>
                  <td>${count.failedCount}</td>
                </tr>
              </#list>
              </tbody>
            </table>
          </div>
        </#if>

    </#if>

</div>

</body>
</html>