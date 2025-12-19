plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    `maven-publish`
}

group = "com.global.commercial-trading"
version = rootProject.version

repositories {
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    implementation(gradleApi())
}

gradlePlugin {
    plugins {
        create("commercialTradingCheckstyle") {
            id = "com.gpo.commercialtrading.checkstyle"
            implementationClass = "com.gpo.gradle.checkstyle.CommercialTradingCheckstylePlugin"
            displayName = "Commercial Trading Checkstyle Plugin"
            description = "Automatically downloads and configures checkstyle for commercial trading projects"
        }
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    jvmToolchain(17)
}

publishing {
    repositories {
        mavenLocal()

        maven {
            name = "Nexus"
            url = uri(
                if (version.toString().endsWith("SNAPSHOT")) {
                    "https://nexus.shared.global.com/repository/maven-snapshots/"
                } else {
                    "https://nexus.shared.global.com/repository/maven-releases/"
                }
            )
        }
    }

    publications.withType<MavenPublication>().configureEach {
        pom {
            name.set("Commercial Trading Checkstyle Plugin")
            description.set("Gradle plugin for applying commercial trading checkstyle standards")
            url.set("https://github.com/global-engineering-shared/commercial-trading-checkstyle")

            licenses {
                license {
                    name.set("Internal Use Only")
                }
            }

            developers {
                developer {
                    organization.set("Global Engineering")
                }
            }
        }
    }
}
