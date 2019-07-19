package demo.app.chat.interfaces;

import demo.app.chat.domain.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

/**
 * Created by itaesu on 19/07/2019.
 */
@RequiredArgsConstructor
@Component
public class RedisPublisher {
    private final RedisTemplate<String, Object> redisTemplate;

    public void pubilsh(ChannelTopic topic, ChatMessage message) {
        redisTemplate.convertAndSend(topic.getTopic(), message);
    }
}
