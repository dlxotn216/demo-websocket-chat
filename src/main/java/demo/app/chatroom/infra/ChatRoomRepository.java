package demo.app.chatroom.infra;

import demo.app.chatroom.domain.ChatRoom;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by itaesu on 19/07/2019.
 *
 * Chatroom을 아래와 같이 저장하기위해 실제로는 동기화 필요
 *
 * Redis 등에 세션을 저장하는 확장을 통해 안정성을 얻어야 함
 *
 * -> Chatroom 내에 세션 정보 역시 저장되어야 하며
 * Session이 만료된 경우 해당 Session을 제거하는 처리가 필요 함
 */
@Component
public class ChatRoomRepository {
    private Set<ChatRoom> chatRooms = new HashSet<>();

    public Set<ChatRoom> getChatRooms() {
        return this.chatRooms;
    }

    public ChatRoom add(ChatRoom chatRoom) {
        this.chatRooms.add(chatRoom);
        return chatRoom;
    }
}
