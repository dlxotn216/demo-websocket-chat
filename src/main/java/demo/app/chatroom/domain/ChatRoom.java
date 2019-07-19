package demo.app.chatroom.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * Created by itaesu on 19/07/2019.
 */
@Getter @Setter
public class ChatRoom {
    private String roomId;
    private String name;

    public static ChatRoom from(String name){
        ChatRoom chatRoom = new ChatRoom();;
        chatRoom.setRoomId(UUID.randomUUID().toString());
        chatRoom.setName(name);
        return chatRoom;
    }
}
