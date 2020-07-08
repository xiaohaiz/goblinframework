<#-- @ftlvariable name="queueProducerBuilderManager" type="org.goblinframework.queue.producer.builder.QueueProducerBuilderManagerMXBean" -->
<#-- @ftlvariable name="queueConsumerBuilderManager" type="org.goblinframework.queue.consumer.builder.QueueConsumerBuilderManagerMXBean" -->
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
                <td colspan="6"><strong>Queue Producer(s)</strong></td>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td><strong>system</strong></td>
                <td><strong>type</strong></td>
                <td><strong>location</strong></td>
                <td><strong>serializer</strong></td>
                <td><strong>successCount</strong></td>
                <td><strong>failureCount</strong></td>
            </tr>
            <#list queueProducerBuilderManager.queueProducerBuilderList as builder>
                <#if builder.producerList?has_content>
                <#list builder.producerList as producer>
                <tr>
                    <td>${builder.getSystem().name()}</td>
                    <td>${producer.producerType}</td>
                    <td>${producer.location}</td>
                    <td>${producer.serializer}</td>
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

    <#if queueConsumerBuilderManager.queueConsumerBuilderList?has_content>
    <div class="row">
        <table class="table table-bordered table-condensed table-striped">
            <thead>
            <tr class="info">
                <td colspan="14"><strong>Queue Consumer(s)</strong></td>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td><strong>system</strong></td>
                <td><strong>type</strong></td>
                <td><strong>location</strong></td>
                <td><strong>maxConcurrentConsumers</strong></td>
                <td><strong>maxPermits</strong></td>
                <td><strong>group</strong></td>
                <td><strong>fetched</strong></td>
                <td><strong>published</strong></td>
                <td><strong>discarded</strong></td>
                <td><strong>received</strong></td>
                <td><strong>handled</strong></td>
                <td><strong>transformed</strong></td>
                <td><strong>success</strong></td>
                <td><strong>failure</strong></td>
            </tr>
            <#list queueConsumerBuilderManager.queueConsumerBuilderList as builder>
            <#if builder.consumerList?has_content>
            <#list builder.consumerList as consumer>
            <tr>
                <td>${builder.getSystem().name()}</td>
                <td>${consumer.consumerType}</td>
                <td>${consumer.location}</td>
                <td>${consumer.maxConcurrentConsumers}</td>
                <td>${consumer.maxPermits}</td>
                <td>${consumer.group}</td>
                <td>${consumer.fetched}</td>
                <td>${consumer.published}</td>
                <td>${consumer.discarded}</td>
                <td>${consumer.received}</td>
                <td>${consumer.handled}</td>
                <td>${consumer.transformed}</td>
                <td>${consumer.success}</td>
                <td>${consumer.failure}</td>
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