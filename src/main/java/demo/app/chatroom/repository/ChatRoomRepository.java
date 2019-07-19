package demo.app.chatroom.repository;

import demo.app.chat.domain.ChatMessage;
import demo.app.chatroom.domain.ChatRoom;
import demo.app.handler.RedisSubscriber;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by itaesu on 19/07/2019.
 */
@RequiredArgsConstructor
@Component
public class ChatRoomRepository {
    private static final String CHAT_ROOM = "CHAT_ROOM";
    private static final String CHAT_MESSAGE = "CHAT_MESSAGE";

    private final RedisSubscriber redisSubscriber;

    private final RedisMessageListenerContainer redisMessageListenerContainer;

    private final RedisTemplate<String, Object> redisTemplate;

    private HashOperations<String, String, ChatRoom> opsHashChatRoom;

    private Map<String, ChannelTopic> topics;

    @PostConstruct
    public void init(){
        this.opsHashChatRoom = this.redisTemplate.opsForHash();
        topics = new ConcurrentHashMap<>(); //Multi threading을 위해 ConcurrentHashMap으로 변경, (Thread local은 더 적합해보임)
        //어짜피 Channel Topic이 가진 topic (chanel ID를 rawString으로 바꿔 쓰는데 Map이 필요?)
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

//    public ChannelTopic getTopic(String roomId){
//        return this.topics.get(roomId);
//    }

    public ChannelTopic getTopic(String roomId){
        return new ChannelTopic(roomId);
    }
}
