package com.fusionkoding.scfleetsverificationworker.clients;

import com.fusionkoding.scfleetsverificationworker.clients.models.RsiAuth;
import com.fusionkoding.scfleetsverificationworker.config.PropertyConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@RequiredArgsConstructor
@Component
public class AccountClient {

    private final PropertyConfig propertyConfig;

    private final RestTemplate authRestTemplate;

    @Cacheable("accountAuth")
    public RsiAuth getRsiAccountAuthById(String accountId){

        RsiAuth rsiAccountDto = null;

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(propertyConfig.getBaseUrl() + propertyConfig.getAccountUir().getAccountAuthById());

        String urlStr = builder.buildAndExpand(accountId).toUriString();
        log.info("Getting account auth: " + urlStr);

        try {
            RsiAuth response = authRestTemplate.getForObject(urlStr, RsiAuth.class);
            log.info(response.toString());
            rsiAccountDto = response;
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }

        return rsiAccountDto;
    }
}
