import org.gradle.kotlin.dsl.support.uppercaseFirstChar
import org.jetbrains.kotlin.config.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.ktorfit)
    alias(libs.plugins.ksp)
}

kotlin {
    applyDefaultHierarchyTemplate()

    // JVM
    jvm()

    // Android
    androidTarget {
        publishLibraryVariants("release")
        compilations.all {
            kotlinOptions {
                jvmTarget = JvmTarget.JVM_17.description
            }
        }
    }

    // iOS
    val frameworkName = "UMD"
    val xcf = XCFramework(frameworkName)
    val iosTargets = listOf(iosX64(), iosArm64(), iosSimulatorArm64())

    iosTargets.forEach {
        it.binaries.framework {
            baseName = frameworkName
            binaryOption("bundleId", "io.vinicius.umd")
            xcf.add(this)
        }
    }

    // Linux
    linuxX64()
    linuxArm64()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.ktor.serialization.json)
                implementation(libs.ktorfit)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }
    }
}

dependencies {
    add("kspCommonMainMetadata", libs.ktorfit.ksp)

    val targets = listOf("jvm", "android", "iosX64", "iosArm64", "iosSimulatorArm64", "linuxX64", "linuxArm64")
    targets.forEach {
        val config = "ksp${it.uppercaseFirstChar()}"
        val configTest = "${config}Test"
        add(config, libs.ktorfit.ksp)
        add(configTest, libs.ktorfit.ksp)
    }
}

android {
    namespace = "org.jetbrains.kotlinx.multiplatform.library.template"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}