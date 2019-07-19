package demo.app.chatroom.service;

import demo.app.chatroom.domain.ChatMessage;
import demo.app.chatroom.domain.ChatRoom;
import demo.app.chatroom.infra.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Created by itaesu on 19/07/2019.
 */
@Slf4j
@Service @RequiredArgsConstructor
public class ChatRoomPublishService {
    private final ChatRoomRepository chatRoomRepository;

    public void publishMessage(ChatMessage chatMessage) {
        this.publishMessage(chatMessage.getChatRoomId(), chatMessage.getMessage());
    }

    private void publishMessage(String id, String message){
        this.chatRoomRepository.getChatRooms().stream().filter(chatRoom -> chatRoom.getId().equals(id)).findAny()
                .orElseThrow(IllegalArgumentException::new)
                .getSessions()
                .forEach(webSocketSession -> {
                    try {
                        webSocketSession.sendMessage(new TextMessage(message));
                    } catch (IOException e) {
                        log.error("Error sending message [{}]", message, e);
                    }
                });
    }
}
