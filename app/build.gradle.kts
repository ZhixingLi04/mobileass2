plugins {
    // 使用版本目录管理插件版本
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp") version "2.1.0-1.0.29"  // 启用 kapt 插件，确保 Room 注解处理器运行
}

android {
    namespace = "com.example.myapplication_ass2"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.myapplication_ass2"
        minSdk = 35
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                // 如有更新请参考官方文档进行调整
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.transport.api)
    implementation(libs.generativeai)
    // 引用版本目录中定义的 Room runtime（注意：名称已修改为官方推荐的 "room-runtime"）
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.datastore.core.android)

    // 添加 Room 注解处理器依赖
    ksp("androidx.room:room-compiler:2.7.0")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation("androidx.compose.material:material-icons-core:1.5.0")
    implementation("androidx.compose.material:material-icons-extended:1.5.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.1")
    implementation("org.jetbrains.kotlinx:kotlinx-metadata-jvm:0.5.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    testImplementation("junit:junit:4.13.2")
    testImplementation("androidx.test:core:1.5.0")
    testImplementation("androidx.arch.core:core-testing:2.2.0")
    testImplementation("androidx.room:room-testing:2.6.1")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")
    testImplementation("io.mockk:mockk:1.13.4")
    testImplementation("org.robolectric:robolectric:4.8.1")

}