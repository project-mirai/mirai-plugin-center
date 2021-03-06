plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    kotlin("jvm")
    kotlin("plugin.spring")
    kotlin("plugin.serialization")
    java
}

tasks.register("copyDependencies", Copy::class.java) {
    from(project.configurations["runtimeClasspath"])
    destinationDir = project.buildDir.resolve("dependencies")
}

tasks.named("jar") {
    (this as Jar).archiveClassifier.set("original")
}
tasks.getByName("jar").enabled = true

dependencies {
    implementation(project(":"))
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.springframework.boot:spring-boot-starter-security")
    //   implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity5")

    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.1.0")

    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.ehcache:ehcache:3.9.2")
    implementation("javax.cache:cache-api")
    implementation("io.springfox:springfox-boot-starter:3.0.0")

    developmentOnly("org.springframework.boot:spring-boot-devtools")

//
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("org.postgresql:postgresql")
    implementation("com.vladmihalcea:hibernate-types-55:2.12.0")


    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.springframework.security:spring-security-test")

    api("net.mamoe:mirai-console-compiler-annotations:2.6-M2")

    // hibernate
    implementation("org.hibernate:hibernate-jcache:5.4.29.Final")
}
