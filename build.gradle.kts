import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.6.10"
    id("org.jetbrains.compose")
}

repositories {
    mavenCentral()
    google()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven {
        url = uri("https://maven.pkg.github.com/wooodenleg/TmiK")
        credentials {
            username = properties["tmik.username"].toString()
            password = properties["tmik.password"].toString()
        }
    }
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation("com.tmik:tmik-jvm:0.1.2")
    implementation("com.alialbaali.kamel:kamel-image:0.3.0")

    val ktorVersion = "1.6.7"
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("io.ktor:ktor-client-serialization:$ktorVersion")


}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            appResourcesRootDir.set(project.layout.projectDirectory.dir("src/main/resources"))
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "VojtovaTocka"
            packageVersion = "1.0.0"
        }
    }
}