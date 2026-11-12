import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.buildkonfig) // buildConfig的KMP跨平台替代
    alias(libs.plugins.ksp)             // 数据库
    alias(libs.plugins.androidx.room)   // 数据库
    alias(libs.plugins.kotlinSerialization)   // 序列化（导航需要）
}

kotlin {
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }

    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    androidLibrary {
       namespace = "com.rubp.whattoeat.shared"
       compileSdk = libs.versions.android.compileSdk.get().toInt()
       minSdk = libs.versions.android.minSdk.get().toInt()
    
       compilerOptions {
           jvmTarget = JvmTarget.JVM_11
       }
       androidResources {
           enable = true
       }
       withHostTest {
           isIncludeAndroidResources = true
       }
    }
    
    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.activity.compose)
        }
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.compose.icons.material.filled)   // composable icons库
            implementation(libs.compose.icons.material.outlined) // composable icons库
            implementation(libs.coil.compose) // coil库，更好的图片支持
            implementation(libs.coil.network) // coil库，网络图片支持
            implementation(libs.multiplatform.settings)            // 持久化数据
            implementation(libs.multiplatform.settings.no.arg)     // 持久化数据
            implementation(libs.multiplatform.settings.coroutines) // 持久化数据
            implementation(libs.androidx.room.runtime)      // 数据库
            implementation(libs.androidx.sqlite.bundled)    // 数据库
            implementation(libs.kotlinx.serialization.json)   // 导航类型安全路由依赖
            implementation(libs.androidx.navigation.compose)   // 导航
            implementation(libs.kermit.logger) // 日志 API 和默认平台输出
            implementation(libs.kermit.io)     // 文件日志和文件轮换
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

dependencies {
    add("kspAndroid", libs.androidx.room.compiler)
    add("kspIosArm64", libs.androidx.room.compiler)
    add("kspIosSimulatorArm64", libs.androidx.room.compiler)
    androidRuntimeClasspath(libs.compose.uiTooling)
}

buildkonfig {
    packageName = "com.rubp.whattoeat"
    defaultConfigs {
        buildConfigField(STRING, "VERSION_NAME", "0.1.0")
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}