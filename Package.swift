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
            url: "https://github.com/vegidio/umd-lib/releases/download/24.5.0/umd-xcframework.zip",
            checksum: "0667220cdb2ecb557a49cf6319e96887682c43c1ea0d4219325899d5bf2ca5f4"
        )
    ]
)
