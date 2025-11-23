package wowa.myqna.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
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
import wowa.myqna.chat.dto.ChatRequestDto;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    public Flux<String> generateStream(ChatRequestDto chatRequestDto) {

        String text = chatRequestDto.text();
        String userId = chatRequestDto.userId();

        UserMessage userMessage = new UserMessage(text);

        QuestionAnswerAdvisor ragAdvisor = QuestionAnswerAdvisor.builder(vectorStore)
                .searchRequest(SearchRequest.builder()
                        .similarityThreshold(0.3d)
                        .topK(6)
                        .filterExpression("owner == '" + userId + "'")
                        .build())
                .build();

        Prompt prompt = new Prompt(userMessage);

        return chatClient.prompt(prompt)
                .advisors(ragAdvisor)
                .stream()
                .content();
    }
}
