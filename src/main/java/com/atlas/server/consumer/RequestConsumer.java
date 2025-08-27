package com.atlas.server.consumer;

import com.atlas.server.dto.RequestDto;
import com.atlas.server.compensation.CompensationService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class RequestConsumer {

    private final Set<String> processed = ConcurrentHashMap.newKeySet();
    private final CompensationService compensationService;


    @KafkaListener(
            topics = "forwarder-topic",
            groupId = "forwarder-group")
    public void consume(RequestDto dto) {

        String key = dto.getCorporateId();

        if (processed.contains(key)) return;
        processed.add(key);

        try {
            applyBusinessLogic(dto);
        } catch (Exception e) {
            compensationService.execute(dto);
        }
    }

    private void applyBusinessLogic(RequestDto dto) {
        System.out.println(" Final processing for " + dto.getCorporateId());
    }
}
