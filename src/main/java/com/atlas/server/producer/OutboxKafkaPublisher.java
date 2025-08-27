package com.atlas.server.producer;

import com.atlas.server.model.OutboxRequest;
import com.atlas.server.repository.OutboxRequestRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OutboxKafkaPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final OutboxRequestRepository outboxRepository;

    @Scheduled(fixedDelay = 1000)
    @Transactional
    public void publishOutboxRequests() {
        List<OutboxRequest> pending = outboxRepository.findAllByStatus(OutboxRequest.Status.PENDING);

        for (OutboxRequest msg : pending) {
            try {
                kafkaTemplate.send("request-forwarder-topic", msg.getCorporateId(), msg.getPayload()).get();
                msg.setStatus(OutboxRequest.Status.SENT);
            } catch (Exception e) {
                msg.setStatus(OutboxRequest.Status.FAILED);
                kafkaTemplate.send("request-forwarder-dlq", msg.getCorporateId(), msg.getPayload());
            } finally {
                outboxRepository.save(msg);
            }
        }
    }
}
