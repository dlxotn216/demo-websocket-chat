package demo.app.chat.interfaces;

import demo.app.chat.domain.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

/**
 * Created by itaesu on 19/07/2019.
 */
@RequiredArgsConstructor
@Controller
public class ChatPublisher {
    private final SimpMessageSendingOperations messageSendingOperations;

    @MessageMapping("/chat/message")
    public void message(ChatMessage message){
        if(message.isEnterRequest()){
            this.messageSendingOperations.convertAndSend("/sub/chat/room/"+message.getRoomId(),
                                                         new ChatMessage(ChatMessage.Type.TEXT,
                                                                         message.getRoomId(),
                                                                         message.getSender()+" Entered",
                                                                         message.getSender()));
        } else {
            this.messageSendingOperations.convertAndSend("/sub/chat/room/"+message.getRoomId(), message);
        }
    }
}
