package io.morningcode.grpc.client

interface GreetingService {
    fun by(name: String): String
}
