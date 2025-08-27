package com.atlas.server;

import io.grpc.stub.StreamObserver;
import org.springframework.grpc.server.service.GrpcService;


@GrpcService
public class UserService extends UserServiceGrpc.UserServiceImplBase {

    @Override
    public void getUser(UserRequest request, StreamObserver<UserResponse> responseObserver) {
        int userId = request.getUserId();
        String username = "User_" + userId;

        UserResponse response = UserResponse.newBuilder()
                .setUserId(userId)
                .setUsername(username)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
	
}