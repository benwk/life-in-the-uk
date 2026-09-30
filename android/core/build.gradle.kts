plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
    testImplementation(libs.junit)
}

// android/<module> -> repository root
val repoRoot: File = projectDir.parentFile.parentFile

// Tests decode the real content bundle, so build it first.
val buildContentBundle by tasks.registering(Exec::class) {
    val out = layout.buildDirectory.file("content/content.json")
    inputs.dir(repoRoot.resolve("content"))
    inputs.dir(repoRoot.resolve("tools"))
    outputs.file(out)
    commandLine("python3", repoRoot.resolve("tools/build_content.py").path, "--out", out.get().asFile.path)
}

tasks.test {
    dependsOn(buildContentBundle)
    systemProperty("robin.contentBundle", layout.buildDirectory.file("content/content.json").get().asFile.path)
    systemProperty("robin.coreCases", repoRoot.resolve("content/fixtures/core-cases.json").path)
}
