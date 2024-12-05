import org.gradle.kotlin.dsl.support.zipTo
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig
import org.jetbrains.kotlin.incremental.createDirectory

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.multiplatform)
}

val appVersion = "1.0.1"
val appVersionCode = 2

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    jvm("desktop")

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        moduleName = "composeApp"
        browser {
            val rootDirPath = project.rootDir.path
            val projectDirPath = project.projectDir.path
            commonWebpackConfig {
                outputFileName = "composeApp.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(rootDirPath)
                        add(projectDirPath)
                    }
                }
            }
        }
        binaries.executable()
    }

    sourceSets {
        val desktopMain by getting

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.kotlinx.datetime)
            implementation(libs.jetbrains.lifecycle.viewmodel)
            implementation(libs.jetbrains.lifecycle.runtime.compose)
            implementation(libs.napier)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
        }
    }
}

android {
    namespace = "eu.heha.meditation"
    compileSdk = libs.versions.android.target.sdk.get().toInt()

    defaultConfig {
        applicationId = "eu.heha.meditation"
        minSdk = libs.versions.android.min.sdk.get().toInt()
        targetSdk = libs.versions.android.target.sdk.get().toInt()
        versionCode = appVersionCode
        versionName = appVersion
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "eu.heha.meditation.Meditation"

        buildTypes.release.proguard {
            configurationFiles.from(project.file("compose-desktop.pro"))
        }

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "eu.heha.meditation"
            packageVersion = appVersion
        }
    }
}

tasks.register<CopyDesktopArtifacts>("buildDesktopRelease") {
    group = "release"
    intoFolder.set(rootDir.resolve("releases"))
    artefactName.set("Meditation.$appVersion")
    dependsOn("createReleaseDistributable")
}

abstract class CopyDesktopArtifacts @Inject constructor(
    @Inject private val layout: ProjectLayout
) : DefaultTask() {

    @get:Input
    abstract val intoFolder: Property<File>

    @get:Input
    abstract val artefactName: Property<String>

    @TaskAction
    fun action() {
        val build = layout.buildDirectory.get().dir("compose/binaries/main-release/app")
        intoFolder.get().createDirectory()
        val osSlug = when (currentOS) {
            OS.Linux -> "linux"
            OS.Windows -> "windows"
            OS.MacOS -> "macos"
        }
        zipTo(intoFolder.get().resolve("${artefactName.get()}.$osSlug.zip"), build.asFile)
    }

    enum class OS(val id: String) {
        Linux("linux"),
        Windows("windows"),
        MacOS("macos")
    }

    private val currentOS: OS
        get() {
            val os = System.getProperty("os.name")
            return when {
                os.equals("Mac OS X", ignoreCase = true) -> OS.MacOS
                os.startsWith("Win", ignoreCase = true) -> OS.Windows
                os.startsWith("Linux", ignoreCase = true) -> OS.Linux
                else -> error("Unknown OS name: $os")
            }
        }
}