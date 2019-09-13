<#-- @ftlvariable name="CompressorManagerMXBean" type="org.goblinframework.core.compression.CompressorManagerMXBean" -->
<#-- @ftlvariable name="SerializerManagerMXBean" type="org.goblinframework.core.serialization.SerializerManagerMXBean" -->
<!--suppress HtmlUnknownTarget -->
<html>
<head>
  <title>CORE MODULE</title>
</head>
<body>

<h2>CORE MODULE</h2>

<h3>
  <a class="btn btn-default" href="/index.do" role="button">DASHBOARD</a>
</h3>

<div class="container-fluid">

    <#if CompressorManagerMXBean.compressorList?has_content>
      <div class="row">
        <table class="table table-bordered table-condensed table-striped">
          <thead>
          <tr class="info">
            <td colspan="6"><strong>Compressor(s)</strong></td>
          </tr>
          </thead>
          <tbody>
          <tr>
            <td><strong>upTime</strong></td>
            <td><strong>mode</strong></td>
            <td><strong>compress</strong></td>
            <td><strong>compressException</strong></td>
            <td><strong>decompress</strong></td>
            <td><strong>decompressException</strong></td>
          </tr>
          <#list CompressorManagerMXBean.compressorList as m>
            <tr>
              <td>${m.upTime}</td>
              <td>${m.getMode().name()}</td>
              <td>${m.compressCount}</td>
              <td>${m.compressExceptionCount}</td>
              <td>${m.decompressCount}</td>
              <td>${m.decompressExceptionCount}</td>
            </tr>
          </#list>
          </tbody>
        </table>
      </div>
    </#if>

    <#if SerializerManagerMXBean.serializerList?has_content>
      <div class="row">
        <table class="table table-bordered table-condensed table-striped">
          <thead>
          <tr class="info">
            <td colspan="6"><strong>Serializer(s)</strong></td>
          </tr>
          </thead>
          <tbody>
          <tr>
            <td><strong>upTime</strong></td>
            <td><strong>mode</strong></td>
            <td><strong>serialize</strong></td>
            <td><strong>serializeException</strong></td>
            <td><strong>deserialize</strong></td>
            <td><strong>deserializeException</strong></td>
          </tr>
          <#list SerializerManagerMXBean.serializerList as s>
            <tr>
              <td>${s.upTime}</td>
              <td>${s.getMode().name()}</td>
              <td>${s.serializeCount}</td>
              <td>${s.serializeExceptionCount}</td>
              <td>${s.deserializeCount}</td>
              <td>${s.deserializeExceptionCount}</td>
            </tr>
          </#list>
          </tbody>
        </table>
      </div>
    </#if>

</div>

</body>
</html>