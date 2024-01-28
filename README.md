# Universal Media Downloader

A [KMP library](https://github.com/Kotlin/multiplatform-library-template) to easily gather links from media files hosted on different websites.

It supports the following targets/languages:

|                                                 Target                                                  |        Language (Repository)         |
|:-------------------------------------------------------------------------------------------------------:|:------------------------------------:|
|      ![](https://img.shields.io/badge/JVM-7F52FF?&style=for-the-badge&logo=kotlin&logoColor=white)      |            Kotlin (Maven)            |
|    ![](https://img.shields.io/badge/Android-34A853?style=for-the-badge&logo=android&logoColor=white)    |            Kotlin (Maven)            |
|       ![](https://img.shields.io/badge/iOS-FFFFFF?style=for-the-badge&logo=apple&logoColor=black)       | Kotlin Native (Maven) / Swift (SPM)  |
|      ![](https://img.shields.io/badge/macOS-000000?style=for-the-badge&logo=macos&logoColor=white)      | Kotlin Native (Maven) / Swift (SPM)  |
|      ![](https://img.shields.io/badge/Linux-FCC624?style=for-the-badge&logo=linux&logoColor=black)      |        Kotlin Native (Maven)         |
|    ![](https://img.shields.io/badge/Windows-0078D4?style=for-the-badge&logo=windows&logoColor=white)    |        Kotlin Native (Maven)         |
| ![](https://img.shields.io/badge/TypeScript-3178C6?style=for-the-badge&logo=typescript&logoColor=white) | Kotlin JS (Maven) / ESM module (NPM) |

## ⬇️ Download

### Maven (Kotlin Multiplatform / Android)

This library is hosted in my own Maven repository, so before using it in your project you must add the repository `https://maven.vinicius.io` to your `settings.gradle.kts` file:

```kotlin
dependencyResolutionManagement {
    //...
    repositories {
        google()
        mavenCentral()
        maven { setUrl("https://maven.vinicius.io") } // Add this line
    }
    //...
}
```

With the repository added, you just need to include the dependency in the file `build.gradle.kts`:

```kotlin
//...
dependencies {
    implementation("io.vinicius.umd:umd:24.1.28")
}
//...
```

## 📝 License

**umd-lib** is released under the MIT License. See [LICENSE](LICENSE) for details.

## 👨🏾‍💻 Author

Vinicius Egidio ([vinicius.io](http://vinicius.io))