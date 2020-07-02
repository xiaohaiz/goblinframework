package org.goblinframework.rpc.module.converter;

import org.goblinframework.api.annotation.Install;
import org.goblinframework.core.util.HttpUtils;
import org.goblinframework.core.util.NumberUtils;
import org.goblinframework.rpc.registry.RpcClientNode;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;

import java.util.LinkedHashMap;

@Install
public class StringToRpcClientNodeConverter implements Converter<String, RpcClientNode> {

  @NotNull
  @Override
  public RpcClientNode convert(@NotNull String source) {
    LinkedHashMap<String, String> map = HttpUtils.parseQueryString(source);
    RpcClientNode node = new RpcClientNode();
    node.setClientId(map.get("clientId"));
    node.setClientName(map.get("clientName"));
    node.setClientHost(map.get("clientHost"));
    node.setClientPid(NumberUtils.toInt(map.get("clientPid")));
    return node;
  }
}
