import org.gradle.kotlin.dsl.support.uppercaseFirstChar
import org.jetbrains.kotlin.config.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ktorfit)
    alias(libs.plugins.ksp)
    alias(libs.plugins.skie)
    alias(libs.plugins.ktlint)

    `maven-publish`
}

kotlin {
    applyDefaultHierarchyTemplate()

    // JVM
    jvm()

    // JavaScript
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
        mavenPublication { artifactId = "android" }
        compilations.all {
            kotlinOptions {
                jvmTarget = JvmTarget.JVM_17.description
            }
        }
    }

    // iOS & macOS
    val frameworkName = "UMD"
    val xcf = XCFramework(frameworkName)
    val appleTargets = listOf(iosArm64(), iosSimulatorArm64(), iosX64(), macosArm64(), macosX64())

    appleTargets.forEach {
        it.binaries.framework {
            baseName = frameworkName
            binaryOption("bundleId", "io.vinicius.umd")
            xcf.add(this)
        }
    }

    // Linux
    linuxArm64()
    linuxX64()

    // Windows
    mingwX64()

    sourceSets {
        // Common
        commonMain.dependencies {
            implementation(libs.coroutines.core)
            implementation(libs.kermit)
            implementation(libs.kotlin.datetime)
            implementation(libs.ksoup)
            implementation(libs.ktor.client.negotiation)
            implementation(libs.ktor.serialization.json)
            implementation(libs.ktorfit)
            implementation(libs.okio)
            implementation(libs.skie.annotations)
            implementation(libs.slf4j)
        }

        commonTest.dependencies {
            implementation(libs.coroutines.test)
            implementation(libs.kotlin.test)
        }

        // Android
        androidMain {
            dependsOn(nativeMain.get())
            dependencies {
                implementation(libs.ktor.engine.jvm)
            }
        }

        // Apple (iOS, macOS)
        appleMain.dependencies {
            implementation(libs.ktor.engine.apple)
        }

        // Linux
        linuxMain.dependencies {
            implementation(libs.ktor.engine.linux)
        }

        // Windows
        mingwMain.dependencies {
            implementation(libs.ktor.engine.windows)
        }

        // JVM
        jvmMain {
            dependsOn(nativeMain.get())
            dependencies {
                implementation(libs.ktor.engine.jvm)
            }
        }

        // JavaScript
        jsMain.dependencies {
            implementation(libs.coroutines.js)
            implementation(libs.ktor.engine.js)
            implementation(libs.okio.node)
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

// Disable Skie analytics
skie {
    analytics {
        enabled.set(false)
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

    val targets = kotlin.targets.names.filter { it != "metadata" }
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

group = "io.vinicius.umd"
version = System.getenv("VERSION") ?: "1.0-SNAPSHOT"

afterEvaluate {
    apply(from = "../publish.gradle.kts")
}