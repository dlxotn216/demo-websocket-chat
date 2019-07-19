package demo.app.chat.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Created by itaesu on 19/07/2019.
 */
@Getter @AllArgsConstructor @NoArgsConstructor
public class ChatMessage {
    private Type type;
    private String roomId;
    private String message;
    private String sender;

    public boolean isEnterRequest() {
        return type == Type.ENTER;
    }
    public enum Type {
        ENTER,
        TEXT;
    }
}
