package wowa.myqna.embedding.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record EmbeddingRequestDto(
        @NotBlank
        String myName,
        @NotNull
        MultipartFile multipartFile
) {
}
