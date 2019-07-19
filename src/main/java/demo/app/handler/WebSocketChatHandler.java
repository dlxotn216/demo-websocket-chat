package demo.app.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import demo.app.chatroom.domain.ChatMessage;
import demo.app.chatroom.infra.ChatRoomRepository;
import demo.app.chatroom.service.ChatRoomPublishService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * Created by itaesu on 19/07/2019.
 */
@Slf4j
@Component @RequiredArgsConstructor
public class WebSocketChatHandler extends TextWebSocketHandler {
    private final ChatRoomPublishService chatRoomPublishService;
    private final ChatRoomRepository chatRoomRepository;
    private final ObjectMapper objectMapper;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        final String payload = message.getPayload();
        ChatMessage chatMessage = this.objectMapper.readValue(payload, ChatMessage.class);
        if(chatMessage.isEnterMessage()){
            this.chatRoomRepository.getChatRooms().stream().filter(chatRoom -> chatMessage.getChatRoomId().equals(chatMessage.getChatRoomId()))
                    .findAny()
                    .orElseThrow(IllegalArgumentException::new)
                    .addSession(session);
        }
        this.chatRoomPublishService.publishMessage(chatMessage);
    }
}
