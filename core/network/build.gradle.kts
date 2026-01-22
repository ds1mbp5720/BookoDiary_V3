import java.io.FileInputStream
import java.util.Properties
import kotlin.apply

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

val localProperties = Properties().apply {
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        load(FileInputStream(localPropertiesFile))
    }
}

android {
    namespace = "lee.project.core.network"
    compileSdk = 35

    defaultConfig {
        minSdk = 33

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        val ttbKey = localProperties.getProperty("TTB_KEY") ?: ""
        // 3. BuildConfig에 심기 (코드에서 사용 가능하게)
        buildConfigField("String", "TTB_KEY", ttbKey)
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
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
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.gson)
    // --- Retrofit & OkHttp ---
    implementation(libs.squareup.retrofit)
    implementation(libs.squareup.retrofit.gson)
    implementation(libs.squareup.okhttp3)
    implementation(libs.squareup.okhttp3.logging)
    // --- Moshi (JSON Converter) ---
    implementation(libs.squareup.retrofit.converter.moshi)
    implementation(libs.squareup.moshi)
    // --- Hilt ---
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    // --- Test ---
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk)
    testImplementation(libs.squareup.retrofit)
    testImplementation(libs.squareup.mockserver)
    testImplementation(libs.squareup.moshi)
}