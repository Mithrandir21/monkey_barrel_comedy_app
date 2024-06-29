import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.sqldelight)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    jvm("desktop")

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "domain"
            isStatic = true
        }
    }

    task("testClasses") // Required because of bug in KMM plugin

    sourceSets {

        commonMain.dependencies {
            //put your multiplatform dependencies here
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.serialization.properties)
            implementation(libs.kotlinx.datetime)

            implementation(libs.koin.core)
            implementation(libs.stately.common) // TODO - https://github.com/cashapp/sqldelight/issues/4357#issuecomment-1839905700

            implementation(libs.napier)

            implementation(libs.sqldelight.extensions.coroutines)

            implementation(project(":logging"))
            implementation(project(":common"))
            implementation(project(":remote"))
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }

        androidMain.dependencies {
            implementation(libs.sqldelight.android)
        }

        iosMain.dependencies {
            implementation(libs.sqldelight.native)
        }

        val desktopMain by getting {
            dependencies {
                implementation(libs.sqldelight.jvm)
            }
        }
    }
}

android {
    namespace = "pm.bam.mbc.domain"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

sqldelight {
    databases {
        create("Database") {
            packageName.set("pm.bam.mbc.domain")
        }
    }
}