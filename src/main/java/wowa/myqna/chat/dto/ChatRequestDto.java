package wowa.myqna.chat.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChatRequestDto(
        @Size(min = 1, max = 1000, message = "채팅은 1자 이상 100자 이하로 입력할 수 있습니다.")
        String text,
        @NotBlank(message = "userId는 필수입니다.")
        String userId,
        @NotBlank(message = "roomId는 필수입니다.")
        String roomId
) {
}
