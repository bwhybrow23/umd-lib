# Usage

With the library properly [installed](../installation), you just need to initialize the class `Umd()` passing the URL of the website that you want to fetch media information. **UMD** will automatically detect what site/content you're trying to fetch media information; if the URL is not supported then it will throw an exception.

If everything goes well and **UMD** detects the URL, you can use the instantiated object to call the methods below:

## queryMedia()

=== "Kotlin"

    ```kotlin
    import io.vinicius.umd.Umd

    val umd = Umd("https://www.reddit.com/user/atomicbrunette18")
    val response = umd.queryMedia()
    ```

=== "Swift"

    ```swift
    import UMD

    let umd = Umd(url: "https://coomer.su/onlyfans/user/atomicbrunette18")
    let response = try? await umd.queryMedia()
    ```

=== "TypeScript"

    Coming soon...
