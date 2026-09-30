// swift-tools-version: 5.9
import PackageDescription

// Pure Swift logic shared by the app: content models, grading, mock exams.
// Mirrors android/core; both run content/fixtures/core-cases.json.
let package = Package(
    name: "RobinCore",
    platforms: [.iOS(.v17), .macOS(.v14)],
    products: [
        .library(name: "RobinCore", targets: ["RobinCore"]),
    ],
    targets: [
        .target(name: "RobinCore"),
        .testTarget(name: "RobinCoreTests", dependencies: ["RobinCore"]),
    ]
)
