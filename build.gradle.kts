import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.3.61"
    application
    id("com.github.johnrengelman.shadow") version "5.0.0"
}

repositories {
    jcenter()
    maven { url = uri("https://maven.pterodactylus.net/") }
    mavenLocal()
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    compile(group = "io.ktor", name = "ktor-server-netty", version = "1.3.1")
    compile(group = "io.ktor", name = "ktor-jackson", version = "1.3.1")

    compile(group = "net.pterodactylus", name = "jFCPlib", version = "0.1.7-SNAPSHOT")

    testImplementation(group = "org.junit.jupiter", name = "junit-jupiter-api", version = "5.6.0")
    testImplementation(group = "org.junit.platform", name = "junit-platform-runner", version = "1.6.0")
    testImplementation(group = "org.junit.jupiter", name = "junit-jupiter-engine", version = "5.6.0")
    testImplementation(group = "org.hamcrest", name = "hamcrest-all", version = "1.3")
}

application {
    mainClassName = "net.pterodactylus.fresta.FrestaKt"
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
    withType<Jar> {
        manifest {
            attributes(
                mapOf("Main-Class" to application.mainClassName)
            )
        }
    }
    withType<Test> {
        useJUnitPlatform()
    }
}
