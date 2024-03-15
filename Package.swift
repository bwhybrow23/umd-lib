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
            url: "https://github.com/vegidio/umd-lib/releases/download/24.3.15/umd-xcframework.zip",
            checksum: "29cd5510b4ad153f9cedbe9cfec024924fd5b68abf3b226ff2e3a9f07ba2c834"
        )
    ]
)
