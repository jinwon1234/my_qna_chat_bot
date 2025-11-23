package wowa.myqna.chat.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import wowa.myqna.chat.domain.ChatMessage;
import wowa.myqna.chat.dto.ChatRequestDto;
import wowa.myqna.chat.service.ChatService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/api/chats/rooms")
    public ResponseEntity<Void> createChatroom(@RequestParam String userId) {

        String chatroomId = UUID.randomUUID().toString();

        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", "/chat/" + userId + "/" + chatroomId)
                .build();
    }

    @PostMapping("/chats/stream")
    public Flux<String> streamChat(@Valid @RequestBody ChatRequestDto chatRequestDto) {
        return chatService.generateStream(chatRequestDto);
    }

    @GetMapping("/chats/history/{roomId}")
    public ResponseEntity<List<ChatMessage>> getChatHistory(@PathVariable String roomId) {

        List<ChatMessage> response = chatService.findChatMessageByRoomId(roomId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
