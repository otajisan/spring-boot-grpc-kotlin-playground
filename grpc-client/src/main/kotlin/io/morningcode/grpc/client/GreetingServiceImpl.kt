package io.morningcode.grpc.client

import io.grpc.ManagedChannelBuilder
import io.morningcode.grpc.proto.GreetingRequest
import io.morningcode.grpc.proto.GreetingServiceGrpc
import io.morningcode.grpc.proto.GreetingServiceGrpcKt
import org.springframework.stereotype.Service

@Service
class GreetingServiceImpl : GreetingService {

    override fun by(name: String): String {
        val channel = ManagedChannelBuilder.forAddress("localhost", 19090).usePlaintext().build()
        val stub = GreetingServiceGrpc.newBlockingStub(channel)
        val request = GreetingRequest.newBuilder().setName(name).build()

        val response = stub.greetBy(request)
        return response.message
    }

}
