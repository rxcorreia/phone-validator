import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("plugin.jpa") version "1.2.71"
    id("org.springframework.boot") version "2.1.5.RELEASE"
    id("io.spring.dependency-management") version "1.0.7.RELEASE"
    kotlin("jvm") version "1.2.71"
    kotlin("plugin.spring") version "1.2.71"
}

group = "pt.olx.challenge"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    compile("org.springframework.boot:spring-boot-starter-actuator")
    compile("org.springframework.boot:spring-boot-starter-data-jpa")
    compile("org.springframework.boot:spring-boot-starter-web")
    compile("org.jetbrains.kotlin:kotlin-reflect")
    compile("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    compile("com.fasterxml.jackson.module:jackson-module-kotlin")
    compile("org.flywaydb:flyway-core")
    compile("org.hibernate:hibernate-core")
    compile("org.hibernate:hibernate-entitymanager")
    compile("org.postgresql:postgresql")

    testCompile("org.springframework.boot:spring-boot-starter-test")
    testCompile("org.springframework.security:spring-security-test")
    testCompile("com.nhaarman:mockito-kotlin:1.6.0")
    testCompile("com.opentable.components:otj-pg-embedded:0.12.0")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}
