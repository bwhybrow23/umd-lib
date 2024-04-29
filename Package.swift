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
            url: "https://github.com/vegidio/umd-lib/releases/download/24.4.5/umd-xcframework.zip",
            checksum: "3a17f4ac82f42496b2d2ae51422ce52e0c04876df152dfb2e82eb5501abb1f15"
        )
    ]
)
