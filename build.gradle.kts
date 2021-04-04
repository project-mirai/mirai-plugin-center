import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.4.4"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.4.31"
	kotlin("plugin.spring") version "1.4.31"
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

		tasks.withType<KotlinCompile> {
			kotlinOptions {
                freeCompilerArgs = listOf("-Xjsr305=strict")
                jvmTarget = "11"
            }
        }

        tasks.withType<Test> {
            useJUnitPlatform()
        }
    }
}

extensions.findByName("buildScan")?.withGroovyBuilder {
    setProperty("termsOfServiceUrl", "https://gradle.com/terms-of-service")
    setProperty("termsOfServiceAgree", "yes")
}
