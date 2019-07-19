package demo.app.chatroom.interfaces;

import demo.app.chatroom.domain.ChatRoom;
import demo.app.chatroom.service.ChatRoomCreateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by itaesu on 19/07/2019.
 */
@RestController @RequiredArgsConstructor
public class ChatRoomCreateController {
    private final ChatRoomCreateService chatRoomCreateService;

    @PostMapping("/chatrooms")
    public ResponseEntity<ChatRoom> createChatRoom(@RequestParam("name") String name){
        return ResponseEntity.ok(this.chatRoomCreateService.createChatRoom(name));
    }
}
