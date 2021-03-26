package com.fusionkoding.scfleetsverificationworker.consumers;

import com.fusionkoding.scfleetsverificationworker.clients.SpectrumClient;
import com.fusionkoding.scfleetsverificationworker.clients.exceptions.RsiAuthException;
import com.fusionkoding.scfleetsverificationworker.config.RabbitMQConfig;
import com.fusionkoding.scfleetsverificationworker.dtos.AuthNotify;
import com.fusionkoding.scfleetsverificationworker.dtos.Verify;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMqReceiver {

    private final Binding verifyBinding;
    private final RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry;
    private final RabbitMQConfig rabbitMQConfig;
    private final RabbitTemplate rabbitTemplate;
    private final SpectrumClient spectrumClient;

    @RabbitListener(queues = "${spring.rabbitmq.queue.verify}")
    public void receivedVerifyMessage(Message<Verify> verifyMessage) {
        Verify request = verifyMessage.getPayload();

        log.info(request.getAccountId());
        log.info(request.getRsiHandle());
        log.info(request.getPilotId());
        log.info(request.getVerificationCode());

        try {
            String memberId = spectrumClient.getMemberId(request.getRsiHandle(), request.getAccountId());
            String lobbyId = spectrumClient.getLobbyId(memberId, request.getAccountId());
            String messageId = spectrumClient.sendMessage(lobbyId, request.getVerificationCode(), request.getAccountId());
            log.info(memberId);
            log.info(lobbyId);
            log.info(messageId);
        }catch (Exception exception) {
            stopQueue(rabbitMQConfig.getVerifyQueue());
            rabbitTemplate.convertAndSend(rabbitMQConfig.getAuthExchange(),"status",AuthNotify.builder().authenticated(false).build());
        }
    }

    @RabbitListener(queues = "#{rabbitMQConfig.getAuthQueue()}" )
    public void receivedAuthNotifyMessage(Message<AuthNotify> authNotifyMessage) {
        boolean authenticated = authNotifyMessage.getPayload().isAuthenticated();
        log.info("Authenticated: " + authenticated);
        if(!authenticated){
            stopQueue(rabbitMQConfig.getVerifyQueue());
        } else {
            startQueue(rabbitMQConfig.getVerifyQueue());
        }
    }

    private void stopQueue(String queueName) {
        rabbitListenerEndpointRegistry.getListenerContainers().stream().forEach(c -> {
            SimpleMessageListenerContainer simpleMessageListenerContainer = (SimpleMessageListenerContainer) c;
            String id = Arrays.stream(simpleMessageListenerContainer.getQueueNames()).findFirst().get();
            if(id.equals(queueName)){
                simpleMessageListenerContainer.stop();
            }
        });
    }

    private void startQueue(String queueName) {
        rabbitListenerEndpointRegistry.getListenerContainers().stream().forEach(c -> {
            SimpleMessageListenerContainer simpleMessageListenerContainer = (SimpleMessageListenerContainer) c;
            String id = Arrays.stream(simpleMessageListenerContainer.getQueueNames()).findFirst().get();
            if(id.equals(queueName)){
                simpleMessageListenerContainer.start();
            }
        });
    }

}
