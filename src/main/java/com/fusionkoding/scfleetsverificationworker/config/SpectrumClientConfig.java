package com.fusionkoding.scfleetsverificationworker.config;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SpectrumClientConfig {
    private String baseUrl;
    private String messageUri;
    private String lobbyInfoUri;
    private String autocompleteUri;
}
