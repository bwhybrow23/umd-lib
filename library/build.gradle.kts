import org.gradle.kotlin.dsl.support.uppercaseFirstChar
import org.jetbrains.kotlin.config.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ktorfit)
    alias(libs.plugins.ksp)
    alias(libs.plugins.ktlint)
}

kotlin {
    applyDefaultHierarchyTemplate()

    // JVM
    jvm()

    js {
        moduleName = "umd-lib"
        nodejs()
        binaries.library()
        useEsModules()
        generateTypeScriptDefinitions()
    }

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
        // Common
        commonMain.dependencies {
            implementation(libs.coroutines.core)
            implementation(libs.kotlin.datetime)
            implementation(libs.ktor.client.negotiation)
            implementation(libs.ktor.serialization.json)
            implementation(libs.ktorfit)
            implementation(libs.okio)
        }
        commonTest.dependencies {
            implementation(libs.coroutines.test)
            implementation(libs.kotlin.test)
        }

        // JVM, Android depend on Native
        jvmMain.configure { dependsOn(nativeMain.get()) }
        androidMain.configure { dependsOn(nativeMain.get()) }

        // JavaScript
        jsMain.dependencies {
            implementation(libs.coroutines.js)
        }

        all {
            languageSettings.optIn("kotlin.js.ExperimentalJsExport")
        }
    }
}

android {
    namespace = "io.vinicius.umd"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}

// Workaround for KSP picking the wrong Java version
afterEvaluate {
    tasks.withType<JavaCompile>().configureEach {
        sourceCompatibility = JavaVersion.VERSION_17.toString()
        targetCompatibility = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    add("kspCommonMainMetadata", libs.ktorfit.ksp)

    val targets = listOf("jvm", "js", "android", "iosX64", "iosArm64", "iosSimulatorArm64", "linuxX64", "linuxArm64")
    targets.forEach {
        val config = "ksp${it.uppercaseFirstChar()}"
        val configTest = "${config}Test"
        add(config, libs.ktorfit.ksp)
        add(configTest, libs.ktorfit.ksp)
    }
}

configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
    additionalEditorconfig.set(
        mapOf("ktlint_code_style" to "intellij_idea"),
    )
    filter {
        exclude {
            it.file.path.contains("generated")
        }
    }
}