package wowa.myqna.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wowa.myqna.chat.domain.ChatMessage;
import wowa.myqna.chat.repository.ChatMessageRepository;

import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class ChatMessageLowService {

    private final ChatMessageRepository chatMessageRepository;

    public List<ChatMessage> findByRoomIdOrderByCreatedAtAsc(String userId) {
        return chatMessageRepository.findByRoomIdOrderByCreatedAtAsc(userId);
    }

    public ChatMessage save(ChatMessage chatMessage) {
        return chatMessageRepository.save(chatMessage);
    }
}
