import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }

//        // https://youtrack.jetbrains.com/issue/KT-46452/Allow-to-run-common-tests-as-Android-Instrumentation-tests
//        // https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-test.html
//        @OptIn(ExperimentalKotlinGradlePluginApi::class)
//        instrumentedTestVariant {
//            sourceSetTree.set(KotlinSourceSetTree.test)
//
//            dependencies {
//                implementation(libs.compose.test.ui.junit4)
//                debugImplementation(libs.compose.test.ui.manifest)
//            }
//        }
    }

    jvm("desktop")

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "artists"
            isStatic = true
        }
    }

    task("testClasses") // Required because of bug in KMM plugin

    sourceSets {

        commonMain.dependencies {
            //put your multiplatform dependencies here
            implementation(compose.ui)
            implementation(compose.runtime)
            implementation(compose.material3)
            implementation(compose.foundation)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)

            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.datetime)

            implementation(libs.jetbrains.androidx.lifecycle.runtime.compose)
            implementation(libs.jetbrains.androidx.lifecycle.viewmodel.compose)
            implementation(libs.jetbrains.androidx.navigation.compose)

            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            implementation(libs.coil.mp)
            implementation(libs.coil.compose)
            implementation(libs.coil.compose.core)
            implementation(libs.coil.network.ktor)

            implementation(libs.napier)

            implementation(project(":logging"))
            implementation(project(":common"))
            implementation(project(":domain"))
            implementation(project(":compose"))
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)

            implementation(libs.koin.test)

            implementation(libs.kotlinx.coroutines.test)

            implementation(libs.kotest.engine)
            implementation(libs.kotest.asserts)
            implementation(libs.kotest.property)

            implementation(libs.turbine)

//            @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
//            implementation(compose.uiTest)

            implementation(project(":testing"))
        }

        androidMain.dependencies {
            implementation(libs.kotlinx.coroutines.android)
        }

        iosMain.dependencies {
        }

        val iosX64Main by getting {
            dependencies {
                implementation(libs.kotlinx.coroutines.iosx64)
            }
        }
        val iosArm64Main by getting {
            dependencies {
                implementation(libs.kotlinx.coroutines.iosarm64)
            }
        }
        val iosSimulatorArm64Main by getting {
            dependencies {
                implementation(libs.kotlinx.coroutines.iossimulatorarm64)
            }
        }

        val desktopMain by getting {
            dependencies {
                implementation(libs.kotlinx.coroutines.swing)
            }
        }
        val desktopTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
                implementation(compose.desktop.currentOs)
            }
        }
    }
}

android {
    namespace = "pm.bam.mbc.feature.artists"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()

//        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}