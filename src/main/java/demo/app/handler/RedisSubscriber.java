package demo.app.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import demo.app.chat.domain.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.Nullable;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Created by itaesu on 19/07/2019.
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class RedisSubscriber implements MessageListener {
    private final ObjectMapper objectMapper;
    private final RedisTemplate redisTemplate;
    private final SimpMessageSendingOperations messageSendingOperations;
    @Override
    public void onMessage(Message message, @Nullable byte[] bytes) {
        try {
            String publishedMessage = (String) this.redisTemplate.getStringSerializer().deserialize(message.getBody());
            ChatMessage chatMessage = this.objectMapper.readValue(publishedMessage, ChatMessage.class);
            this.messageSendingOperations.convertAndSend("/sub/chat/room/"+chatMessage.getRoomId(), chatMessage);
        } catch (IOException e){
            log.error(e.getMessage());
        }
    }
}
