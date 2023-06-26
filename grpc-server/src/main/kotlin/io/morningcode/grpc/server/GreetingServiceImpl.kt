package io.morningcode.grpc.server

import io.morningcode.grpc.proto.GreetingRequest
import io.morningcode.grpc.proto.GreetingResponse
import io.morningcode.grpc.proto.GreetingServiceGrpcKt
import org.lognet.springboot.grpc.GRpcService

@GRpcService
class GreetingServiceImpl : GreetingServiceGrpcKt.GreetingServiceCoroutineImplBase() {

    override suspend fun greetBy(request: GreetingRequest): GreetingResponse =
        GreetingResponse.newBuilder()
            .setMessage("Hello, ${request.name}!")
            .build()
}
