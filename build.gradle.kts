plugins {
    kotlin("multiplatform") version "1.7.10"
}

group = "tech.zone84.examples"
version = "1.0-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()
}

kotlin {
    linuxArm32Hfp("native") {
        compilations {
            "main" {
                cinterops {
                    val bcm2835 by cinterops.creating {
                        defFile("src/nativeInterop/cinterop/bcm2835.def")
                        includeDirs("src/nativeInterop/cinterop/")
                    }
                }
            }
        }
        binaries {
            executable {
                entryPoint = "main"
            }
        }
    }
    sourceSets {
        val nativeMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-datetime-linuxarm32hfp:0.4.0-SNAPSHOT")
            }
        }
    }
}
