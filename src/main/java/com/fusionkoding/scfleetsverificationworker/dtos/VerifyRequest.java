package com.fusionkoding.scfleetsverificationworker.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VerifyRequest {
    private String accountId;
    private String verificationCode;
    private String pilotId;
    private String rsiHandle;
}
