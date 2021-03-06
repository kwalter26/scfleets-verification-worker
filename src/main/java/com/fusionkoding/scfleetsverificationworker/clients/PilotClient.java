package com.fusionkoding.scfleetsverificationworker.clients;

import com.fusionkoding.scfleetsverificationworker.config.PropertyConfig;
import com.fusionkoding.scfleetsverificationworker.dtos.PilotInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@RequiredArgsConstructor
@Component
public class PilotClient {

    private final PropertyConfig propertyConfig;

    private final RestTemplate authRestTemplate;

    public void updatePilot(String pilotId, String rsiHandle, PilotInfoDto pilotInfoDto) {

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(propertyConfig.getPilotUri().getUpdatePilotRsiProfile());

        HttpEntity<PilotInfoDto> entity = new HttpEntity<>(pilotInfoDto, headers);

        String urlStr = builder.buildAndExpand(pilotId, rsiHandle).toUriString();
        log.info("Updating pilot: " + urlStr);

        try {
            String response = authRestTemplate.patchForObject(urlStr, entity, String.class);
            log.info(response);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
    }

}
