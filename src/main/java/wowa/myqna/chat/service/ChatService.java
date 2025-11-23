package wowa.myqna.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import wowa.myqna.chat.domain.ChatMessage;
import wowa.myqna.chat.dto.ChatRequestDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;
    private final ChatMemory chatMemory;
    private final ChatMessageLowService chatMessageLowService;


    public Flux<String> generateStream(ChatRequestDto chatRequestDto) {

        String text = chatRequestDto.text();
        String userId = chatRequestDto.userId();
        String roomId = chatRequestDto.roomId();

        saveUserMessage(roomId, text);

        QuestionAnswerAdvisor ragAdvisor = QuestionAnswerAdvisor.builder(vectorStore)
                .searchRequest(SearchRequest.builder()
                        .similarityThreshold(0.3d)
                        .topK(6)
                        .filterExpression("owner == '" + userId + "'")
                        .build())
                .build();

        Prompt prompt = new Prompt(chatMemory.get(roomId));

        StringBuffer messageBuffer = new StringBuffer();

        return chatClient.prompt(prompt)
                .advisors(ragAdvisor)
                .stream()
                .content()
                .mapNotNull(token-> {
                    messageBuffer.append(token);
                    return token;
                })
                .doOnComplete(() -> {
                    String assistantMessage = messageBuffer.toString();
                    saveAssistantMessage(roomId, assistantMessage);
                });
    }

    public List<ChatMessage> findChatMessageByRoomId(String roomId) {
        return chatMessageLowService.findByRoomIdOrderByCreatedAtAsc(roomId);
    }

    private void saveUserMessage(String roomId, String userMessage) {
        ChatMessage userMessageEntity = new ChatMessage(roomId, MessageType.USER, userMessage);
        chatMemory.add(roomId, new UserMessage(userMessage));
        chatMessageLowService.save(userMessageEntity);
    }

    private void saveAssistantMessage(String roomId, String assistantMessage) {
        chatMemory.add(roomId, new AssistantMessage(assistantMessage));

        ChatMessage assistantMessageEntity = new ChatMessage(roomId, MessageType.ASSISTANT, assistantMessage);
        chatMessageLowService.save(assistantMessageEntity);
    }
}
