<#-- @ftlvariable name="eventBusWorkerMXBean" type="org.goblinframework.core.event.EventBusWorkerMXBean" -->
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
</h3>

<div class="container-fluid">

    <#if eventBusWorkerMXBean??>
        <#assign worker=eventBusWorkerMXBean>
      <div class="row">
        <table class="table table-bordered table-condensed table-striped">
          <thead>
          <tr class="info">
            <td colspan="10"><strong>Event bus worker</strong></td>
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
          </tr>
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
          </tr>
          </tbody>
        </table>
      </div>

        <#if worker.eventListenerList?has_content>
          <div class="row">
            <table class="table table-bordered table-condensed table-striped">
              <thead>
              <tr class="info">
                <td colspan="7"><strong>Event listener(s)</strong></td>
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
                <td><strong>detail</strong></td>
              </tr>
              <#list worker.eventListenerList as listener>
                <tr>
                  <td>${listener.upTime}</td>
                  <td>${listener.listener}</td>
                  <td>${listener.acceptedCount}</td>
                  <td>${listener.rejectedCount}</td>
                  <td>${listener.succeedCount}</td>
                  <td>${listener.failedCount}</td>
                  <td>
                    <a class="btn btn-default"
                       href="/goblin/event/listener.do?workerId=${worker.id}&listenerId=${listener.id}"
                       role="button"><span class="glyphicon glyphicon-eye-open"></span></a>
                  </td>
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