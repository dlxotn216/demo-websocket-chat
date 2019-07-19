package demo.app.chatroom.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by itaesu on 19/07/2019.
 */
@Getter @Setter @NoArgsConstructor
public class ChatMessage {
    private MessageType type;
    private String chatRoomId;
    private String message;

    public boolean isEnterMessage() {
        return type == MessageType.ENTER;
    }

    public enum MessageType {
        ENTER,
        TEXT;
    }
}
