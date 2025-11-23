package wowa.myqna.embedding.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wowa.myqna.embedding.dto.EmbeddingRequestDto;
import wowa.myqna.embedding.dto.EmbeddingResponseDto;
import wowa.myqna.embedding.service.EmbeddingService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/embeddings")
public class EmbeddingController {

    private final EmbeddingService embeddingService;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Void> embedding(@ModelAttribute @Valid EmbeddingRequestDto embeddingRequestDto) {

        EmbeddingResponseDto response = embeddingService.embeddingPdf(embeddingRequestDto);

        return ResponseEntity
                .status(HttpStatus.FOUND)
                .header("Location", "/link/" + response.uuid())
                .build();
    }
}
