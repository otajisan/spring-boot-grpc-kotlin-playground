import com.google.protobuf.gradle.id
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.1.0"
    id("io.spring.dependency-management") version "1.1.0"
    kotlin("jvm") version "1.8.21"
    kotlin("plugin.spring") version "1.8.21"
    // https://plugins.gradle.org/plugin/com.google.cloud.tools.jib
    id("com.google.cloud.tools.jib") version "3.3.2"
    // https://plugins.gradle.org/plugin/com.google.protobuf
    id("com.google.protobuf") version "0.9.3"
    // https://github.com/LogNet/grpc-spring-boot-starter
    id("io.github.lognet.grpc-spring-boot") version "5.1.2"
    jacoco
}

group = "io.morningcode"
version = "0.0.1-SNAPSHOT"

val buildNumber by extra("1")

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

jib {

    from {
        image = "openjdk:17-jdk-slim-bullseye"
    }

    to {
        image =
            System.getenv("AWS_ACCOUNT_ID") + ".dkr.ecr." + System.getenv("AWS_REGION") + ".amazonaws.com/grpc-server"
        tags = setOf("$version.${extra["buildNumber"]}")
    }

    container {
        creationTime.set("USE_CURRENT_TIMESTAMP")
        jvmFlags = listOf(
            "-Xms512m",
            "-Xmx1024m",
            "-Duser.language=ja",
            "-Duser.timezone=Asia/Tokyo",
            "-Dspring.devtools.restart.enabled=false",
        )
        environment = mapOf(
            "SPRING_PROFILES_ACTIVE" to "production"
        )
        workingDirectory = "/grpc-server"
        volumes = listOf("/data")
        ports = listOf(
            "19090",
        )
    }
}

// see: https://repo.maven.apache.org/maven2/com/google/protobuf/protoc/
val protobufVersion = "3.23.2"
val grpcVersion = "1.56.0"
val grpcKotlinVersion = "1.3.0"

dependencies {
    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter")
    // gRPC
    implementation("io.grpc:grpc-netty:$grpcVersion")
    implementation("io.grpc:grpc-protobuf:$grpcVersion")
    implementation("io.grpc:grpc-stub:$grpcVersion")
    implementation("io.grpc:grpc-kotlin-stub:$grpcKotlinVersion")
    implementation("com.google.protobuf:protobuf-java:$protobufVersion")
    implementation("com.google.protobuf:protobuf-kotlin:$protobufVersion")

    // Use common protobuf classes
    //implementation(project(":grpc-interface"))

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}


protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:$protobufVersion"
    }

    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:$grpcVersion"
        }
        id("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:$grpcKotlinVersion:jdk8@jar"
        }
    }

    generateProtoTasks {
        all().forEach {
            it.plugins {
                id("grpc")
                id("grpckt")
            }
        }
    }
}

tasks.named("build").configure {
    dependsOn("generateProto")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging.showStandardStreams = true
}
