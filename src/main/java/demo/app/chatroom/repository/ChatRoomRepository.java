package demo.app.chatroom.repository;

import demo.app.chatroom.domain.ChatRoom;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by itaesu on 19/07/2019.
 */
@Component
public class ChatRoomRepository {
    private Map<String, ChatRoom> chatRoomMap = new LinkedHashMap<>();

    public List<ChatRoom> findAll() {
        return new ArrayList<>(this.chatRoomMap.values());
    }

    public ChatRoom findById(String id) {
        return this.chatRoomMap.get(id);
    }

    public ChatRoom create(String name) {
        final ChatRoom from = ChatRoom.from(name);
        return this.chatRoomMap.put(from.getRoomId(), from);
    }
}
