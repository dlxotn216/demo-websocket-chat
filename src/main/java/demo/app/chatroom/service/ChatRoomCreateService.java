package demo.app.chatroom.service;

import demo.app.chatroom.domain.ChatRoom;
import demo.app.chatroom.infra.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by itaesu on 19/07/2019.
 */
@Slf4j
@Service @RequiredArgsConstructor
public class ChatRoomCreateService {
    private final ChatRoomRepository chatRoomRepository;

    public ChatRoom createChatRoom(String name) {
        return this.chatRoomRepository.add(ChatRoom.builder().id(UUID.randomUUID().toString()).name(name).build());
    }
}
