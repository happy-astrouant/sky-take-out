package com.sky.websocket;


import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
@ServerEndpoint("/ws/{sid}")
public class WebsocketServer {

    private static final Map<String, Session> sessionPool = new HashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) {
        log.info("与客户端-{}建立websocket连接", sid);
        sessionPool.put(sid, session);
    }


    @OnMessage
    public void onMessage(String message, @PathParam("sid") String sid) {
        log.info("收到来自客户端-{}的消息: {}", sid, message);
    }

    // 广播给所有客户端
    public void sendAll(String message) {
        for (String key : sessionPool.keySet()) {
            Session session = sessionPool.get(key);
            try {
                session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                log.error("向客户端-{}发送消息失败: {}", key, e.getMessage());
            }
        }
    }

    // 客户端断开连接
    @OnClose
    public void onClose(@PathParam("sid") String sid) {
        log.info("与客户端-{}断开websocket连接", sid);
        sessionPool.remove( sid);
    }
}
