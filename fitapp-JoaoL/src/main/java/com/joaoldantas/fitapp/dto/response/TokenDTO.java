package com.joaoldantas.fitapp.dto.response;

import lombok.Builder;

@Builder
public record TokenDTO(
        String token,
        long expiraEm,
        UserDetailDTO usuario
) {
    @Builder
    public record UserDetailDTO(
            String email
    ) {
    }
}
