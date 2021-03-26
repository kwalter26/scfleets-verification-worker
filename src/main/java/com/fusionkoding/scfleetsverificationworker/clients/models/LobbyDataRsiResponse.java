package com.fusionkoding.scfleetsverificationworker.clients.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LobbyDataRsiResponse {
    String success;
    String code;
    String msg;
    LobbyData data;
}
