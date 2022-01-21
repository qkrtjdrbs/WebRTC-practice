package realtime.sockettest;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class ConnectionHandler extends TextWebSocketHandler {

    List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    /**
     * 어떤 session이 webSocket에 메시지를 보낼 때 마다 실행
     * sessions list를 순회하며 메시지가 온 세션을 제외한 각 세션에 메시지 전송
     */
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message)
            throws InterruptedException, IOException {
        System.out.println("Got signal - " + message.getPayload());
        for (WebSocketSession webSocketSession : sessions) {
            if (webSocketSession.isOpen() && !session.getId().equals(webSocketSession.getId())) {
                webSocketSession.sendMessage(message);
            }
        }
    }

    /**
     * 브라우저가 websocket과 handshaking 성공하여 connection, session 생성
     * 생성된 session은 sessions list에 들어간다.
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        System.out.println("-----------------------------------------------------------------------------");
        System.out.println("Connection opened!");
        System.out.println("-----------------------------------------------------------------------------");
    }

    /**
     * 브라우저가 connection을 종료할 때 실행
     * session이 sessions list에서 제거된다.
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        System.out.println("-----------------------------------------------------------------------------");
        System.out.println("Connection Closed!");
        System.out.println("-----------------------------------------------------------------------------");
    }
}
