package com.fusionkoding.scfleetsverificationworker.clients;

import com.fusionkoding.scfleetsverificationworker.clients.exceptions.RsiAuthException;
import com.fusionkoding.scfleetsverificationworker.clients.exceptions.RsiRequestException;
import com.fusionkoding.scfleetsverificationworker.clients.models.*;
import com.fusionkoding.scfleetsverificationworker.config.PropertyConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class SpectrumClient {

    private final PropertyConfig propertyConfig;
    private final RestTemplate regularRestTemplate;
    private final AccountClient accountClient;

    public String getMemberId(String rsiHandle, String accountId) throws RsiAuthException, RsiRequestException {

        String memberId = null;

        AutocompleteDto autocompleteDto = AutocompleteDto.builder().text(rsiHandle).build();
        String urlStr = formUrlString(propertyConfig.getSpectrumClient().getBaseUrl() + propertyConfig.getSpectrumClient().getAutocompleteUri());

            HttpEntity entity = getEntity(autocompleteDto, accountId);
            SearchDataRsiResponse response = regularRestTemplate.exchange(urlStr, HttpMethod.POST, entity, new ParameterizedTypeReference<SearchDataRsiResponse>() {
            }).getBody();

            if (response.getSuccess().equals("0")) {
                if (response.getCode().equals("ErrNotAuthenticated")) {
                    accountClient.invalidateAuth();
                    throw new RsiAuthException("ErrNotAuthenticated");
                }
                throw new RsiRequestException("Rsi request unsuccessful");
            }

            SearchData searchData = response.getData();

            Optional<Member> optionalMember = searchData.getMembers().stream().filter(member -> {
                log.info(member.getNickname());
                return member.getNickname().equals(rsiHandle);
            }).findFirst();

            if (optionalMember.isPresent()) {
                Member member = optionalMember.get();
                memberId = member.getId();
                log.info("Found member id from " + member.getNickname() + ": " + memberId);
            }


        return memberId;
    }

    public String getLobbyId(String memberId, String accountId) throws RsiAuthException, RsiRequestException {
        String lobbyId = "";

        LobbyInfoDto lobbyInfoDto = LobbyInfoDto.builder().member_id(memberId).build();
        String urlStr = formUrlString(propertyConfig.getSpectrumClient().getBaseUrl() + propertyConfig.getSpectrumClient().getLobbyInfoUri());

        HttpEntity entity = getEntity(lobbyInfoDto, accountId);
        LobbyDataRsiResponse response = regularRestTemplate.exchange(urlStr, HttpMethod.POST, entity, new ParameterizedTypeReference<LobbyDataRsiResponse>() {
        }).getBody();

        if (response.getSuccess().equals("0")) {
            if (response.getCode().equals("ErrNotAuthenticated")) {
                accountClient.invalidateAuth();
                throw new RsiAuthException("ErrNotAuthenticated");
            }
            throw new RsiRequestException("Rsi request unsuccessful");
        }

        LobbyData lobbyData = response.getData();

        if (lobbyData != null) {
            lobbyId = lobbyData.getId();
        }

        return lobbyId;
    }

    public String sendMessage(String lobbyId, String message, String accountId) throws RsiAuthException, RsiRequestException {
        String messageId = "";

        MessageDto messageDto = buildMessageDto(lobbyId, message);

        String urlStr = formUrlString(propertyConfig.getSpectrumClient().getBaseUrl() + propertyConfig.getSpectrumClient().getMessageUri());


        HttpEntity entity = getEntity(messageDto, accountId);
        LobbyDataRsiResponse response = regularRestTemplate.exchange(urlStr, HttpMethod.POST, entity, new ParameterizedTypeReference<LobbyDataRsiResponse>() {
        }).getBody();

        if (response.getSuccess().equals("0")) {
            if (response.getCode().equals("ErrNotAuthenticated")) {
                accountClient.invalidateAuth();
                throw new RsiAuthException("ErrNotAuthenticated");
            }
            throw new RsiRequestException("Rsi request unsuccessful");
        }

        LobbyData lobbyData = response.getData();

        if (lobbyData != null) {
            messageId = lobbyData.getId();
        }


        return messageId;
    }

    private String formUrlString(String url) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        String urlStr = builder.toUriString();
        log.info("Getting member id: " + urlStr);
        return urlStr;
    }

    private <T> HttpEntity<T> getEntity(T dto, String accountId) {
        HttpHeaders headers = getSpectrumHttpHeaders(accountId);
        return new HttpEntity<>(dto, headers);
    }

    private MessageDto buildMessageDto(String lobbyId, String message) {
        MessageBlock messageBlock = MessageBlock.builder()
                .key("229h2")
                .text(message)
                .type("unstyled")
                .depth(0)
                .inlineStyleRanges(new ArrayList<>())
                .entityRanges(new ArrayList<>())
                .data(new HashMap<>())
                .build();
        List<MessageBlock> messageBlockList = new ArrayList<>();
        messageBlockList.add(messageBlock);

        MessageDto messageDto = MessageDto.builder()
                .lobbyId(lobbyId)
                .contentState(ContentState.builder()
                        .blocks(messageBlockList)
                        .entityMap(new HashMap<>())
                        .build())
                .plaintext(message)
                .mediaId(null)
                .highlightRoleId(null)
                .build();
        return messageDto;
    }

    private HttpHeaders getSpectrumHttpHeaders(String accountId) {
        RsiAuth rsiAuth = accountClient.getRsiAccountAuthById(accountId);
        HttpHeaders headers = getHttpHeaders();
        headers.set("x-rsi-token", rsiAuth.getToken());
        headers.set("x-tavern-id", rsiAuth.getTavernId());
        headers.set("Cookie", "Rsi-XSRF=" + rsiAuth.getXsrf() + "; Rsi-Token=" + rsiAuth.getToken() + "; _rsi_device=" + rsiAuth.getDeviceId());
        return headers;
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }
}
