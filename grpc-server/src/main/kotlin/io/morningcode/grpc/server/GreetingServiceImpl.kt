package io.morningcode.grpc.server

import org.lognet.springboot.grpc.GRpcService

@GRpcService
class GreetingServiceImpl : GreetingServiceGrpcKt.GreetingServiceCoroutineImplBase() {

    override suspend fun greetBy(request: GreetingRequest): GreetingResponse =
        GreetingResponse.newBuilder()
            .setMessage("Hello, ${request.name}!")
            .build()
}
