import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.4.4" apply false
    id("io.spring.dependency-management") version "1.0.11.RELEASE" apply false
    kotlin("jvm") version "1.4.31"
    kotlin("plugin.spring") version "1.4.31" apply false
    java
}

allprojects {
    group = "net.mamoe.mirai"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

subprojects {
    afterEvaluate {
        java.sourceCompatibility = JavaVersion.VERSION_11

        configurations {
            compileOnly {
                extendsFrom(configurations.annotationProcessor.get())
            }
        }

        kotlin.sourceSets.all {
            languageSettings.apply {
                languageVersion = "1.5"
                progressiveMode = true
                val experimentalAnnotations = """
                    kotlin.OptIn
                    kotlin.contracts.ExperimentalContracts
                """.trimIndent().split("\n").filterNot(String::isBlank)
                for (ann in experimentalAnnotations) {
                    useExperimentalAnnotation(ann)
                }
            }
        }

        configureFlattenSourceSets()

        tasks.withType<KotlinCompile> {
            kotlinOptions {
                freeCompilerArgs = freeCompilerArgs + "-Xjsr305=strict"
                freeCompilerArgs = freeCompilerArgs + "-Xjvm-default=all"
                freeCompilerArgs = freeCompilerArgs + "-Xinline-classes"
                jvmTarget = "11"
            }
        }

        tasks.withType<Test> {
            useJUnitPlatform()
        }

        tasks.withType<JavaCompile> {
            options.encoding = "UTF8"
        }
    }
}

fun Project.configureFlattenSourceSets() {
    sourceSets {
        findByName("main")?.apply {
            resources.setSrcDirs(listOf(projectDir.resolve("resources")))
            java.setSrcDirs(listOf(projectDir.resolve("src")))
        }
        findByName("test")?.apply {
            resources.setSrcDirs(listOf(projectDir.resolve("resources")))
            java.setSrcDirs(listOf(projectDir.resolve("test")))
        }
    }
}

extensions.findByName("buildScan")?.withGroovyBuilder {
    setProperty("termsOfServiceUrl", "https://gradle.com/terms-of-service")
    setProperty("termsOfServiceAgree", "yes")
}
