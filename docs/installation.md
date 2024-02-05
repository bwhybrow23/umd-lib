# Installation

This library can be installed in KMP / Android projects (through Maven), but also natively in other platforms such as iOS/macOS (Swift Package Manager) or Node.js (NPM).

## Maven (KMP / Android)

**UMD** is hosted in my own Maven repository, so before using it in your project you must add the repository `https://maven.vinicius.io` to your `settings.gradle.kts` file:

```kotlin title="settings.gradle.kts" linenums="1" hl_lines="5"
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.vinicius.io")
    }
}
```

With the repository added, you just need to include the dependency in the file `build.gradle.kts`:

```kotlin title="build.gradle.kts" linenums="1"
dependencies {
    implementation("io.vinicius.umd:umd:{{ version }}")
}
```

## SwiftPM (iOS / macOS)

To add **UMD** to your Xcode project, select `File > Add Package Dependencies`:

![Xcode](images/spm1.avif)

Enter the repository URL `https://github.com/vegidio/umd-lib` in the upper right corner to the screen and click on the button `Add Package`:

![Xcode](images/spm2.avif)

## NPM (Node.js)

Coming soon...
