package com.fusionkoding.scfleetsverificationworker.config;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Configuration
@ConfigurationProperties(prefix = "com.scfleets")
public class PropertyConfig {
    private String baseUrl;
    private PilotUri pilotUri;
    private AccountUri accountUir;
    private SpectrumClientConfig spectrumClient;
}
