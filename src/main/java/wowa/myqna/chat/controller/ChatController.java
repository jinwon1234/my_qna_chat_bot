package wowa.myqna.chat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class ChatController {

    @PostMapping("/api/chat/rooms")
    public ResponseEntity<Void> createChatroom(@RequestParam String userId) {

        String chatroomId = UUID.randomUUID().toString();

        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", "/chat/" + userId + "/" + chatroomId)
                .build();
    }
}
