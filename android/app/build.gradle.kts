plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "uk.robin.app"
    compileSdk = 35

    defaultConfig {
        applicationId = "uk.robin.app"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "0.1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
    }

    // content.json is generated from /content by tools/build_content.py (see bundleContent below).
    sourceSets["main"].assets.srcDir(layout.buildDirectory.dir("generated/content"))
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}

// android/<module> -> repository root
val repoRoot: File = projectDir.parentFile.parentFile

val bundleContent by tasks.registering(Exec::class) {
    val out = layout.buildDirectory.file("generated/content/content.json")
    inputs.dir(repoRoot.resolve("content"))
    inputs.dir(repoRoot.resolve("tools"))
    outputs.file(out)
    commandLine("python3", repoRoot.resolve("tools/build_content.py").path, "--out", out.get().asFile.path)
}

tasks.named("preBuild") { dependsOn(bundleContent) }

dependencies {
    implementation(project(":core"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    debugImplementation(libs.androidx.compose.ui.tooling)
}
