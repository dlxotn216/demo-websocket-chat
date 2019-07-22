package demo.app.chatroom.interfaces;

import demo.app.chat.domain.ChatMessage;
import demo.app.chat.interfaces.RedisPublisher;
import demo.app.chatroom.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

/**
 * Created by itaesu on 19/07/2019.
 */
@RequiredArgsConstructor
@Controller
public class ChatController {

    private final RedisPublisher redisPublisher;
    private final ChatRoomRepository chatRoomRepository;

    /**
     * websocket "/pub/chat/message"로 들어오는 메시징을 처리한다.
     */
    @MessageMapping("/chat/message")
    public void message(ChatMessage message) {
        if (message.isEnterRequest()) {
            this.chatRoomRepository.enterChatRoom(message.getRoomId());
            return;
        }
        this.redisPublisher.pubilsh(this.chatRoomRepository.getTopic(message.getRoomId()), message);
        // Websocket에 발행된 메시지를 redis로 발행한다(publish)
    }

}