package demo.app.chatroom.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by itaesu on 19/07/2019.
 */
@Getter @Setter
public class ChatRoom implements Serializable {
    private static final long serialVersionUID = 6494678977089006639L;

    private String roomId;
    private String name;

    public static ChatRoom from(String name){
        ChatRoom chatRoom = new ChatRoom();;
        chatRoom.setRoomId(UUID.randomUUID().toString());
        chatRoom.setName(name);
        return chatRoom;
    }
}
