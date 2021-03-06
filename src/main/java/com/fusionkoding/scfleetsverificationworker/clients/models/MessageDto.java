package com.fusionkoding.scfleetsverificationworker.clients.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageDto {
    @JsonProperty("lobby_id")
    private String lobbyId;
    @JsonProperty("content_state")
    private ContentState contentState;
    private String plaintext;
    @JsonProperty("media_id")
    private String mediaId;
    @JsonProperty("highlight_role_id")
    private String highlightRoleId;
}
