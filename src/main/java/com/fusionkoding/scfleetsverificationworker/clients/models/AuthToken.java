package com.fusionkoding.scfleetsverificationworker.clients.models;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthToken {
    private String accessToken;
    private LocalDateTime validUntil;

    public boolean isValid() {
        if(validUntil == null){
            return false;
        }
        return LocalDateTime.now().isBefore(validUntil);
    }
}
