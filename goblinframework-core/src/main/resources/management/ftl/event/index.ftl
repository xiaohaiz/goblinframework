<#-- @ftlvariable name="eventBusBossMXBean" type="org.goblinframework.core.event.boss.EventBusBossMXBean" -->
<!--suppress HtmlUnknownTarget -->
<html lang="zh">
<head>
  <title>EVENT MODULE</title>
</head>
<body>

<h2>EVENT MODULE</h2>

<h3>
  <a class="btn btn-default" href="/index.do" role="button">DASHBOARD</a>
</h3>

<div class="container-fluid">

  <div class="row">
    <table class="table table-bordered table-condensed table-striped">
      <thead>
      <tr class="info">
        <td colspan="10"><strong>Event bus boss</strong></td>
      </tr>
      </thead>
      <tbody>
      <tr>
        <td><strong>upTime</strong></td>
        <td><strong>bufferSize</strong></td>
        <td><strong>remaining</strong></td>
        <td><strong>workers</strong></td>
        <td><strong>published</strong></td>
        <td><strong>discarded</strong></td>
        <td><strong>received</strong></td>
        <td><strong>workerMissed</strong></td>
        <td><strong>listenerMissed</strong></td>
        <td><strong>dispatched</strong></td>
      </tr>
      <tr>
        <td>${eventBusBossMXBean.upTime}</td>
        <td>${eventBusBossMXBean.bufferSize}</td>
        <td>${eventBusBossMXBean.remainingCapacity}</td>
        <td>${eventBusBossMXBean.workers}</td>
        <td>${eventBusBossMXBean.publishedCount}</td>
        <td>${eventBusBossMXBean.discardedCount}</td>
        <td>${eventBusBossMXBean.receivedCount}</td>
        <td>${eventBusBossMXBean.workerMissedCount}</td>
        <td>${eventBusBossMXBean.listenerMissedCount}</td>
        <td>${eventBusBossMXBean.discardedCount}</td>
      </tr>
      </tbody>
    </table>
  </div>

    <#if eventBusBossMXBean.eventBusWorkerList?has_content>
      <div class="row">
        <table class="table table-bordered table-condensed table-striped">
          <thead>
          <tr class="info">
            <td colspan="7"><strong>Event bus worker(s)</strong></td>
          </tr>
          </thead>
          <tbody>
          <tr>
            <td><strong>upTime</strong></td>
            <td><strong>channel</strong></td>
            <td><strong>bufferSize</strong></td>
            <td><strong>remaining</strong></td>
            <td><strong>workers</strong></td>
            <td><strong>published</strong></td>
            <td><strong>discarded</strong></td>
          </tr>
          <#list eventBusBossMXBean.eventBusWorkerList as worker>
            <tr>
              <td>${worker.upTime}</td>
              <td>${worker.channel}</td>
              <td>${worker.bufferSize}</td>
              <td>${worker.remainingCapacity}</td>
              <td>${worker.workers}</td>
              <td>${worker.publishedCount}</td>
              <td>${worker.discardedCount}</td>
            </tr>
          </#list>
          </tbody>
        </table>
      </div>
    </#if>

</div>

</body>
</html>