package demo.app.chatroom.domain;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by itaesu on 19/07/2019.
 */
@Getter @Builder
public class ChatRoom {
    private String id;

    private String name;

    @Builder.Default
    private List<WebSocketSession> sessions = new ArrayList<>();

    public void addSession(WebSocketSession webSocketSession) {
        this.sessions.add(webSocketSession);
    }
}
