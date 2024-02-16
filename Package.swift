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
            url: "https://github.com/vegidio/umd-lib/releases/download/24.2.16/umd-xcframework.zip",
            checksum: "7a55554e5c7819ee03136c5eb23935d80b5b3d47273def8514e579f6ac2b37de"
        )
    ]
)
