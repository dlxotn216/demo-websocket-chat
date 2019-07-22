package demo.app.chat.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by itaesu on 19/07/2019.
 */
@Getter @AllArgsConstructor @NoArgsConstructor
public class ChatMessage implements Serializable{
    private static final long serialVersionUID = 6424678977089006639L;


    private Type type;
    private String roomId;
    private String message;
    private String sender;
    private String downloadPath;

    public boolean isEnterRequest() {
        return type == Type.ENTER;
    }
    public enum Type {
        FILE,
        ENTER,
        TEXT;
    }
}
