package com.fusionkoding.scfleetsverificationworker.config;

import com.fusionkoding.scfleetsverificationworker.clients.AuthClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class RestTemplateConifg {

    private final AuthClient authClient;


    public RestTemplateConifg(@Value("${basic-auth}") String basicAuth, @Value("${auth-url}") String authUrl, @Value("${grant}") String grant, @Value("${scope}") String scope) {
        this.authClient = new AuthClient(basicAuth, authUrl, grant, scope, restTemplate());
    }

    @Bean
    CognitoAuthInterceptor cognitoAuthInterceptor() {
        return new CognitoAuthInterceptor(authClient);
    }

    @Bean
    public RestTemplate authRestTemplate() {
        RestTemplate restTemplate = restTemplate();

        List<ClientHttpRequestInterceptor> interceptors
                = restTemplate.getInterceptors();
        if (CollectionUtils.isEmpty(interceptors)) {
            interceptors = new ArrayList<>();
        }
        interceptors.add(cognitoAuthInterceptor());
        restTemplate.setInterceptors(interceptors);

        return restTemplate;
    }

    @Bean
    public RestTemplate regularRestTemplate(){
        return restTemplate();
    }

    private RestTemplate restTemplate() {
        return new RestTemplate(new HttpComponentsClientHttpRequestFactory());
    }
}
