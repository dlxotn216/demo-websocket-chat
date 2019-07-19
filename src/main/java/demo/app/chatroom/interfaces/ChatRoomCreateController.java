package demo.app.chatroom.interfaces;

import demo.app.chatroom.domain.ChatRoom;
import demo.app.chatroom.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by itaesu on 19/07/2019.
 */
@RequiredArgsConstructor
@RestController
public class ChatRoomCreateController {
    private final ChatRoomRepository chatRoomRepository;

    @PostMapping("/chat/room")
    public ChatRoom createRoom(@RequestParam("name") String name) {
        return this.chatRoomRepository.create(name);
    }

}
