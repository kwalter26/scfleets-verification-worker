package com.fusionkoding.scfleetsverificationworker.clients;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Component
public class RsiSiteClient {

    private static final String GET_PILOT = "https://robertsspaceindustries.com/citizens/";
    private final RestTemplate regularRestTemplate;

    public String getPilotInfo(String rsiPilotId) {
        return regularRestTemplate.getForObject(GET_PILOT + rsiPilotId, String.class);
    }

}
