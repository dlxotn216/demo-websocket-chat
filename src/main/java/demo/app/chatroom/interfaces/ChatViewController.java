package demo.app.chatroom.interfaces;

import demo.app.chatroom.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Created by itaesu on 19/07/2019.
 */
@RequiredArgsConstructor
@Controller
public class ChatViewController {
    private final ChatMessageRepository chatMessageRepository;

    @GetMapping("/chat/room")
    public String rooms(Model model) {
        return "/chat/room";
    }

    @GetMapping("/chat/room/enter/{roomId}")
    public String roomDetail(Model model, @PathVariable String roomId) {
        model.addAttribute("roomId", roomId);
        return "/chat/roomdetail";
    }

}
