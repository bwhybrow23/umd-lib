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
            url: "https://github.com/vegidio/umd-lib/releases/download/24.4.7/umd-xcframework.zip",
            checksum: "a4fc25ee6e22336aebd91ea2e9e8c98d3d03221a085aa3f8d5fc4667aed1a877"
        )
    ]
)
