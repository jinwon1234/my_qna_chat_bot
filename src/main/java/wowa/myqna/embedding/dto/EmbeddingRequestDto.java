package wowa.myqna.embedding.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record EmbeddingRequestDto(
        @NotBlank(message = "이름은 필수값입니다.")
        String myName,
        @NotNull(message = "PDF 파일은 필수값입니다.")
        MultipartFile multipartFile
) {
}
