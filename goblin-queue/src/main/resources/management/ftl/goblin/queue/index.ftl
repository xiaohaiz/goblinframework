<#-- @ftlvariable name="queueProducerBuilderManager" type="org.goblinframework.queue.producer.builder.QueueProducerBuilderManagerMXBean" -->
<!--suppress HtmlUnknownTarget -->
<html lang="zh">
<head>
    <title>QUEUE MODULE</title>
</head>
<body>

<h2>QUEUE MODULE</h2>

<h3>
    <a class="btn btn-default" href="/index.do" role="button">DASHBOARD</a>
</h3>

<div class="container-fluid">

    <#if queueProducerBuilderManager.queueProducerBuilderList?has_content>

    <div class="row">
        <table class="table table-bordered table-condensed table-striped">
            <thead>
            <tr class="info">
                <td colspan="2"><strong>Cache(s)</strong></td>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td><strong>system</strong></td>
                <td><strong>name</strong></td>
            </tr>
            <#list cacheBuilderManager.cacheBuilderList as builder>
            <#if builder.cacheList?has_content>
            <#list builder.cacheList as cache>
            <tr>
                <td>${cache.getCacheSystem().name()}</td>
                <td>${cache.getCacheName()}</td>
            </tr>
            </#list>
        </#if>
    </#list>
    </tbody>
    </table>
</div>
</#if>

</div>

</body>
</html>