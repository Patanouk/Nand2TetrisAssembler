plugins {
    kotlin("jvm") version "1.9.21"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.xenomachina:kotlin-argparser:2.0.7")
    testImplementation("org.junit.jupiter:junit-jupiter:5.7.1")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}

application() {
    mainClass.set("MainKt")
}