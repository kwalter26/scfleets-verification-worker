package com.fusionkoding.scfleetsverificationworker.clients;

import com.fusionkoding.scfleetsverificationworker.clients.models.AuthResponse;
import com.fusionkoding.scfleetsverificationworker.clients.models.AuthToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
public class AuthClient {

    private final String basicAuth;
    private final String authUrl;
    private final String grant;
    private final String scope;
    private final RestTemplate restTemplate;

    private AuthToken authToken;

    public String getAuthToken() {
        if (authToken != null && authToken.isValid()) {
            return authToken.getAccessToken();
        }
        AuthResponse authResponse = authenticate();
        if (authResponse.getAccessToken() != null && authResponse.getExpiresIn() != null) {
            authToken = AuthToken.builder().accessToken(authResponse.getAccessToken()).validUntil(LocalDateTime.now().plusSeconds(authResponse.getExpiresIn())).build();
        }
        return authToken.getAccessToken();
    }

    private AuthResponse authenticate() {
        HttpEntity entity = new HttpEntity(getHeaders());
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(authUrl);
        uriComponentsBuilder.queryParam("grant_type", grant);
        uriComponentsBuilder.queryParam("scope", scope);
        String url = uriComponentsBuilder.toUriString();
        try {
            ResponseEntity<AuthResponse> authResponse = restTemplate.postForEntity(url, entity, AuthResponse.class);
            return authResponse.getBody();
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return null;
        }

    }

    private HttpHeaders getHeaders() {
        String encodedCredentials =
                new String(Base64.encodeBase64(basicAuth.getBytes()));

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Basic " + encodedCredentials);
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        return httpHeaders;
    }

}
