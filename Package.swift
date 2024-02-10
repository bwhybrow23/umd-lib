// swift-tools-version: 5.7
// The swift-tools-version declares the minimum version of Swift required to build this package.

import PackageDescription

let package = Package(
    name: "UMD",
    platforms: [
        .iOS(.v16),
        .macOS(.v13)
    ],
    products: [
        .library(name: "UMD", targets: ["UMD"])
    ],
    targets: [
        .binaryTarget(
            name: "UMD",
            url: "https://github.com/vegidio/umd-lib/releases/download/24.2.10/umd-xcframework.zip",
            checksum: "0f1d75af067bd14374f2ddabbaada3f02b62b3f15286047472ccc2ec846268d0"
        )
    ]
)
