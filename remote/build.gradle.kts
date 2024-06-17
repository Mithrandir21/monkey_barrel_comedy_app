import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.ktorfit)
}


val supabaseUrlUrl = "SUPABASE_URL"
val supabaseUrlKey = "SUPABASE_KEY"

val supaBaseUrl = gradleLocalProperties(rootDir).getProperty(supabaseUrlUrl) ?: ""
val supaBaseKey = gradleLocalProperties(rootDir).getProperty(supabaseUrlKey) ?: ""


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
            baseName = "remote"
            isStatic = true
        }
    }

    task("testClasses") // Required because of bug in KMM plugin

    sourceSets {

        commonMain.dependencies {
            //put your multiplatform dependencies here
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.kotlinx.serialization)
            implementation(libs.ktorfit.lib)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.datetime)

            implementation(libs.koin.core)

            implementation(project.dependencies.platform(libs.supabase.bom.kt))
            implementation(libs.supabase.postgrest.kt)

            implementation(libs.napier)

            implementation(project(":logging"))
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }

        androidMain.dependencies {
            implementation(libs.ktor.client.okhttp)
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }

        val desktopMain by getting {
            dependencies {
                implementation(libs.ktor.client.apache5)
                implementation(libs.slf4j.simple)
            }
        }
    }
}

dependencies {
    add("kspCommonMainMetadata", libs.ktorfit.ksp)
    add("kspDesktop", libs.ktorfit.ksp)
    add("kspAndroid", libs.ktorfit.ksp)

    add("kspIosX64", libs.ktorfit.ksp)
    add("kspIosArm64",libs.ktorfit.ksp)
    add("kspIosSimulatorArm64",libs.ktorfit.ksp)

}

android {
    namespace = "pm.bam.mbc.remote"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()

        buildConfigField(type = "String", name = supabaseUrlUrl, value = supaBaseUrl)
        buildConfigField(type = "String", name = supabaseUrlKey, value = supaBaseKey)
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        buildConfig = true
    }
}