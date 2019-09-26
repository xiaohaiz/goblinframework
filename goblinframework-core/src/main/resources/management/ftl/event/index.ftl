<#-- @ftlvariable name="eventBusBossMXBean" type="org.goblinframework.core.event.EventBusBossMXBean" -->
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
      <#assign boss=eventBusBossMXBean>
      <tr>
        <td>${boss.upTime}</td>
        <td>${boss.bufferSize}</td>
        <td>${boss.remainingCapacity}</td>
        <td>${boss.workers}</td>
        <td>${boss.publishedCount}</td>
        <td>${boss.discardedCount}</td>
        <td>${boss.receivedCount}</td>
        <td>${boss.workerMissedCount}</td>
        <td>${boss.listenerMissedCount}</td>
        <td>${boss.discardedCount}</td>
      </tr>
      </tbody>
    </table>
  </div>

    <#if eventBusBossMXBean.eventBusWorkerList?has_content>
      <div class="row">
        <table class="table table-bordered table-condensed table-striped">
          <thead>
          <tr class="info">
            <td colspan="11"><strong>Event bus worker(s)</strong></td>
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
            <td><strong>received</strong></td>
            <td><strong>succeed</strong></td>
            <td><strong>failed</strong></td>
            <td><strong>detail</strong></td>
          </tr>
          <#list eventBusBossMXBean.eventBusWorkerList as worker>
            <tr>
              <td>${worker.upTime}</td>
              <td>${worker.channel} (${worker.eventListenerList?size})</td>
              <td>${worker.bufferSize}</td>
              <td>${worker.remainingCapacity}</td>
              <td>${worker.workers}</td>
              <td>${worker.publishedCount}</td>
              <td>${worker.discardedCount}</td>
              <td>${worker.receivedCount}</td>
              <td>${worker.succeedCount}</td>
              <td>${worker.failedCount}</td>
              <td>
                  <#if worker.eventListenerList?has_content>
                    <a class="btn btn-default" href="/goblin/event/worker.do?workerId=${worker.id}"
                       role="button"><span class="glyphicon glyphicon-eye-open"></span></a>
                  <#else>
                    <a href="?">
                      <button type="button" class="btn btn-default" disabled="disabled"><span
                                class="glyphicon glyphicon-eye-open"></span></button>
                    </a>
                  </#if>
              </td>
            </tr>
          </#list>
          </tbody>
        </table>
      </div>
    </#if>

</div>

</body>
</html>