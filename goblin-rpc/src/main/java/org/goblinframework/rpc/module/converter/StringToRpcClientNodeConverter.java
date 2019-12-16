package org.goblinframework.rpc.module.converter;

import org.goblinframework.api.annotation.Install;
import org.goblinframework.core.util.HttpUtils;
import org.goblinframework.core.util.NumberUtils;
import org.goblinframework.remote.core.registry.RemoteClientNode;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;

import java.util.LinkedHashMap;

@Install
public class StringToRpcClientNodeConverter implements Converter<String, RemoteClientNode> {

  @NotNull
  @Override
  public RemoteClientNode convert(@NotNull String source) {
    LinkedHashMap<String, String> map = HttpUtils.parseQueryString(source);
    RemoteClientNode node = new RemoteClientNode();
    node.setClientId(map.get("clientId"));
    node.setClientName(map.get("clientName"));
    node.setClientHost(map.get("clientHost"));
    node.setClientPid(NumberUtils.toInt(map.get("clientPid")));
    return node;
  }
}
