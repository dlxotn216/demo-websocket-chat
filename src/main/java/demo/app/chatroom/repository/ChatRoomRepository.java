package demo.app.chatroom.repository;

import demo.app.chatroom.domain.ChatRoom;
import demo.app.handler.RedisSubscriber;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * Created by itaesu on 19/07/2019.
 */
@RequiredArgsConstructor
@Component
public class ChatRoomRepository {
    private static final String CHAT_ROOM = "CHAT_ROOM";

    private final RedisSubscriber redisSubscriber;

    private final RedisMessageListenerContainer redisMessageListenerContainer;

    private final RedisTemplate<String, Object> redisTemplate;

    private HashOperations<String, String, ChatRoom> opsHashChatRoom;

    private Map<String, ChannelTopic> topics;

    @PostConstruct
    public void init(){
        this.opsHashChatRoom = this.redisTemplate.opsForHash();
        topics = new HashMap<>();
    }

    public List<ChatRoom> findAll() {
        return new ArrayList<>(this.opsHashChatRoom.values(CHAT_ROOM));
    }

    public ChatRoom findById(String id) {
        return this.opsHashChatRoom.get(CHAT_ROOM, id);
    }

    public ChatRoom create(String name) {
        final ChatRoom chatRoom = ChatRoom.from(name);
        this.opsHashChatRoom.put(CHAT_ROOM, chatRoom.getRoomId(), chatRoom);
        return chatRoom;
    }

    public void enterChatRoom(String roomId){
        ChannelTopic topic = this.topics.getOrDefault(roomId, new ChannelTopic(roomId));
        this.redisMessageListenerContainer.addMessageListener(this.redisSubscriber, topic);
        this.topics.put(roomId, topic);
    }

    public ChannelTopic getTopic(String roomId){
        return this.topics.get(roomId);
    }
}
