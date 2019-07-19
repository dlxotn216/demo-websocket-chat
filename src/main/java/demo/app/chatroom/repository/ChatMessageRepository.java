package demo.app.chatroom.repository;

import demo.app.chat.domain.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * Created by itaesu on 19/07/2019.
 */
@RequiredArgsConstructor
@Component
public class ChatMessageRepository {
    private static final String CHAT_MESSAGE = "CHAT_MESSAGE";

    private final RedisTemplate<String, ChatMessage> redisTemplate;

    private ListOperations<String, ChatMessage> listOperations;


    @PostConstruct
    public void init() {
        this.listOperations = this.redisTemplate.opsForList();
    }

    public void save(String roomId, ChatMessage chatMessage) {
        this.listOperations.rightPush(roomId, chatMessage);
    }

    public List<ChatMessage> findByChatRoomId(String roomId) {
        Long size = this.listOperations.size(roomId);
        if (size == null || size == 0) {
            return emptyList();
        }

        List<ChatMessage> range = this.listOperations.range(roomId, 0, size);
        return isEmpty(range)
                ? emptyList()
                : range;
    }

}
