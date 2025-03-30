plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.vsial.eva.visualeva"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.vsial.eva.visualeva"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(project(":domain-core"))

    implementation(project(":domain-camera"))
    implementation(project(":data-camera"))

    implementation(project(":domain-photos"))
    implementation(project(":data-photos"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Material
    implementation(libs.material3)
    implementation(libs.androidx.material.icons.extended)


    // Jetpack Navigation
    implementation(libs.androidx.navigation.compose)

    // Hilt
    implementation(libs.hilt)
    ksp(libs.hilt.compiler)

    // Navigation
    implementation(libs.androidx.hilt.navigation.compose)

    // Serialization
    implementation(libs.org.jetbrains.kotlinx.serialization.core)

    // Coil
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    // CameraX  
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.compose)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.view)
    implementation(libs.androidx.camera.extensions)
    implementation(libs.accompanist.permissions)

    // GPUImage
    implementation("jp.co.cyberagent.android:gpuimage:2.1.0")

}