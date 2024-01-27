configure<PublishingExtension> {
    publications {
        repositories {
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/vegidio/umd-lib")
                credentials {
                    username = System.getenv("GITHUB_ACTOR")
                    password = System.getenv("GITHUB_TOKEN")
                }
            }
        }

        all {
            getByName<MavenPublication>(name) {
                val newArtifact = if (name == "kotlinMultiplatform") "umd" else name
                artifactId = newArtifact.lowercase()
            }
        }
    }
}