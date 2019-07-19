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
@Controller
public class ChatViewController {
    private final ChatRoomRepository chatRoomRepository;

    @GetMapping("/chat/room")
    public String rooms(Model model) {
        return "/chat/room";
    }

    @GetMapping("/chat/rooms")
    @ResponseBody
    public List<ChatRoom> room() {
        return chatRoomRepository.findAll();
    }

    @GetMapping("/chat/room/enter/{roomId}")
    public String roomDetail(Model model, @PathVariable String roomId) {
        model.addAttribute("roomId", roomId);
        return "/chat/roomdetail";
    }

    @PostMapping("/chat/room")
    @ResponseBody
    public ChatRoom createRoom(@RequestParam("name") String name) {
        return this.chatRoomRepository.create(name);
    }

    @GetMapping("/chat/room/{roomId}")
    @ResponseBody
    public ChatRoom roomInfo(@PathVariable String roomId) {
        return chatRoomRepository.findById(roomId);
    }
}
