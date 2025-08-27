package com.atlas.server.scheduler;

import com.atlas.server.model.InboxRequest;
import com.atlas.server.model.OutboxRequest;
import com.atlas.server.repository.InboxRequestRepository;
import com.atlas.server.repository.OutboxRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RequestForwarderScheduler {

    private final InboxRequestRepository inboxRepository;
    private final OutboxRequestRepository outboxRepository;

    @Scheduled(fixedDelay = 2000)
    public void forwardPendingRequests() {
        List<InboxRequest> pending = inboxRepository.findAllByStatus(InboxRequest.Status.PENDING);

        for (InboxRequest inboxMsg : pending) {
            try {
                if (inboxMsg.getRetryCount() > 0) {
                    long delay = (long) Math.pow(2, inboxMsg.getRetryCount()) * 1000L;
                    Thread.sleep(delay);
                }

                processBusiness(inboxMsg);

                OutboxRequest outboxMsg = OutboxRequest.builder()
                        .corporateId(inboxMsg.getCorporateId())
                        .payload(inboxMsg.getPayload())
                        .status(OutboxRequest.Status.PENDING)
                        .createdAt(LocalDateTime.now())
                        .build();

                outboxRepository.save(outboxMsg);

                inboxMsg.setStatus(InboxRequest.Status.SENT);
                inboxRepository.save(inboxMsg);

            } catch (Exception e) {
                inboxMsg.setRetryCount(inboxMsg.getRetryCount() + 1);
                if (inboxMsg.getRetryCount() > 5) {
                    inboxMsg.setStatus(InboxRequest.Status.FAILED);
                }
                inboxRepository.save(inboxMsg);
            }
        }
    }

    private void processBusiness(InboxRequest msg) {
        System.out.println("Processed transaction for " + msg.getCorporateId());
    }
}
