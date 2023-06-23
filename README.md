# spring-boot-grpc-kotlin-playground

Spring Boot x gRPC example

### Server

- launch server application

```bash
./gradlew :server:bootRun
```

- list apis
  - use [grpcurl](https://formulae.brew.sh/formula/grpcurl)

```bash
grpcurl \
  -plaintext \
  -import-path ./grpc-server/src/main/proto \
  -proto GreetingService.proto \
  localhost:19090 list
io.morningcode.grpc.server.GreetingService
```

- send request

```bash
grpcurl \
  -plaintext -d '{"name": "My name"}'\
  -import-path ./grpc-server/src/main/proto \
  -proto GreetingService.proto \
  localhost:19090 io.morningcode.grpc.server.GreetingService/greetBy
{
  "message": "Hello, My name!"
}
```
