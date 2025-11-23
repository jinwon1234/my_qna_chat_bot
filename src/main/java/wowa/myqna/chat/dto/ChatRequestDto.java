package wowa.myqna.chat.dto;

import jakarta.validation.constraints.NotBlank;

public record ChatRequestDto(
        @NotBlank
        String text,
        @NotBlank
        String userId,
        @NotBlank
        String roomId
) {
}
