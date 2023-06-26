package io.morningcode.grpc.client

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class GreetingController(private val greetingService: GreetingService) {

    @GetMapping("/greet/{name}")
    fun by(@PathVariable name: String): String =
        greetingService.by(name)
}
