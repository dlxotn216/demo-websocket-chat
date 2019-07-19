package demo.app.chatroom.interfaces;

import demo.app.chat.domain.ChatMessage;
import demo.app.chatroom.domain.ChatRoom;
import demo.app.chatroom.repository.ChatMessageRepository;
import demo.app.chatroom.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by itaesu on 19/07/2019.
 */
@RequiredArgsConstructor
@RestController
public class ChatRoomSearchController {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    @GetMapping("/chat/rooms")
    public List<ChatRoom> room() {
        return chatRoomRepository.findAll();
    }


    @GetMapping("/chat/room/{roomId}")
    public ChatRoom roomInfo(@PathVariable String roomId) {
        return chatRoomRepository.findById(roomId);
    }

    @GetMapping("/chat/room/{roomId}/messages")
    public List<ChatMessage> getChatMessages(@PathVariable String roomId) {
        return this.chatMessageRepository.findByChatRoomId(roomId);
    }
}
