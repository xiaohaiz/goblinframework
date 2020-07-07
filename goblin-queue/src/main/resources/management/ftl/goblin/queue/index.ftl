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
                <td colspan="5"><strong>Queue Producer(s)</strong></td>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td><strong>system</strong></td>
                <td><strong>type</strong></td>
                <td><strong>definition</strong></td>
                <td><strong>successCount</strong></td>
                <td><strong>failureCount</strong></td>
            </tr>
            <#list queueProducerBuilderManager.queueProducerBuilderList as builder>
                <#if builder.producerList?has_content>
                <#list builder.producerList as producer>
                <tr>
                    <td>${builder.getSystem().name()}</td>
                    <td>${producer.producerType}</td>
                    <td>${producer.definition}</td>
                    <td>${producer.successCount}</td>
                    <td>${producer.failureCount}</td>
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