package com.atlas.server.forwarder;

import com.atlas.server.RequestPayload;
import com.atlas.server.RequestServiceGrpc;
import com.atlas.server.ResponsePayload;
import com.atlas.server.dto.RequestDto;
import com.atlas.server.model.InboxRequest;
import com.atlas.server.repository.InboxRequestRepository;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.grpc.server.service.GrpcService;

import java.time.LocalDateTime;

@GrpcService
@RequiredArgsConstructor
public class RequestHandler extends RequestServiceGrpc.RequestServiceImplBase {

    private final InboxRequestRepository repository;

    @Override
    public void forwardRequest(RequestPayload request, StreamObserver<ResponsePayload> responseObserver) {
        try {

            RequestDto dto = RequestDto.builder()
                    .corporateId(request.getCorporateId())
                    .requestType(request.getType())
                    .payload(request.getPayload())
                    .createdAt(LocalDateTime.now())
                    .build();

            InboxRequest msg = InboxRequest.builder()
                    .corporateId(dto.getCorporateId())
                    .payload(dto.getPayload())
                    .status(InboxRequest.Status.PENDING)
                    .createdAt(LocalDateTime.now())
                    .build();

            repository.save(msg);

            ResponsePayload response = ResponsePayload.newBuilder()
                    .setCorporateId(dto.getCorporateId())
                    .setAccepted(true)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }
}
