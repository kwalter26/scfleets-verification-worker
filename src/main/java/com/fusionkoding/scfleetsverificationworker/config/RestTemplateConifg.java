package com.fusionkoding.scfleetsverificationworker.config;

import com.fusionkoding.scfleetsverificationworker.clients.AuthClient;
import com.fusionkoding.scfleetsverificationworker.clients.RestTemplateResponseErrorHandler;
import com.fusionkoding.scfleetsverificationworker.clients.exceptions.NotAuthorizedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.function.Supplier;

@Configuration
public class RestTemplateConifg {

    private final AuthClient authClient;

    public RestTemplateConifg(@Value("${basic-auth}") String basicAuth, @Value("${auth-url}") String authUrl, @Value("${grant}") String grant, @Value("${scope}") String scope) {
        this.authClient = new AuthClient(basicAuth, authUrl, grant, scope, restTemplate().build());
    }

    @Bean
    CognitoAuthInterceptor cognitoAuthInterceptor() {
        return new CognitoAuthInterceptor(authClient);
    }

    @Bean
    public RestTemplate authRestTemplate() {
        return restTemplate().additionalInterceptors(cognitoAuthInterceptor()).build();
    }

    @Bean
    public RestTemplate regularRestTemplate() throws NotAuthorizedException {
        return restTemplate().build();
    }

    private RestTemplateBuilder restTemplate() {
        return new RestTemplateBuilder()
                .errorHandler(new RestTemplateResponseErrorHandler())
                .requestFactory(HttpComponentsClientHttpRequestFactory.class);
    }
}
