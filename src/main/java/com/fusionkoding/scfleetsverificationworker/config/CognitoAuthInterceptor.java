package com.fusionkoding.scfleetsverificationworker.config;

import com.fusionkoding.scfleetsverificationworker.clients.AuthClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

@RequiredArgsConstructor
public class CognitoAuthInterceptor implements ClientHttpRequestInterceptor {

    private final AuthClient authClient;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        request.getHeaders().set("Authorization", "Bearer " + authClient.getAuthToken());
        return execution.execute(request, body);
    }
}
