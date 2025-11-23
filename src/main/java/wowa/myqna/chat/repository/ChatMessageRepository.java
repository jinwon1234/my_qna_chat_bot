package wowa.myqna.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wowa.myqna.chat.domain.ChatMessage;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findByRoomIdOrderByCreatedAtAsc(String userId);
}
