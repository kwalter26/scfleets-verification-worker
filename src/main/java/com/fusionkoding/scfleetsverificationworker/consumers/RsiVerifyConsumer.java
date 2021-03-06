package com.fusionkoding.scfleetsverificationworker.consumers;


import com.fusionkoding.scfleetsverificationworker.clients.AccountClient;
import com.fusionkoding.scfleetsverificationworker.clients.PilotClient;
import com.fusionkoding.scfleetsverificationworker.clients.SpectrumClient;
import com.fusionkoding.scfleetsverificationworker.dtos.VerifyRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
@Component
public class RsiVerifyConsumer {
    private final SpectrumClient spectrumClient;
    private final PilotClient pilotClient;
    private final AccountClient accountClient;

    @Bean
    public Consumer<Message<VerifyRequest>> rsiVerify() {
        return verifyRequestMessage -> {
            VerifyRequest request = verifyRequestMessage.getPayload();

            log.info(request.getAccountId());
            log.info(request.getRsiHandle());
            log.info(request.getPilotId());
            log.info(request.getVerificationCode());

            String memberId = spectrumClient.getMemberId(request.getRsiHandle(), request.getAccountId());
            String lobbyId = spectrumClient.getLobbyId(memberId, request.getAccountId());
            String messageId = spectrumClient.sendMessage(lobbyId,request.getVerificationCode(),request.getAccountId());
            log.info(memberId);
            log.info(lobbyId);
            log.info(messageId);

//            pilotClient.updatePilot(request.getPilotId(), request.getRsiHandle(), pilot);
        };
    }

}
