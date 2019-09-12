package org.goblinframework.transport.client.handler

enum class TransportClientState {

  CONNECTING,           // 初始化状态
  CONNECT_FAILED,       // 建立连接失败，服务不可用或者建连超时
  CONNECTED,            // 已经完成了建连，但是还没有握手
  HANDSHAKE_FAILED,     // 握手失败
  HANDSHAKED,           // 握手成功，可用状态
  DISCONNECTED,         // 异常导致的连接中断
  HEARTBEAT_LOST,       // 心跳丢失
  SHUTDOWN              // 进入关闭阶段，不再接受新的状态变更


}