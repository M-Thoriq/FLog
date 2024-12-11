plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-parcelize")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("com.google.devtools.ksp")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.thoriq.flog"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.thoriq.flog"
        minSdk = 27
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.2"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    val camerax_version = "1.5.0-alpha03"
    // The following line is optional, as the core library is included indirectly by camera-camera2
    implementation("androidx.camera:camera-core:${camerax_version}")
    implementation("androidx.camera:camera-camera2:${camerax_version}")
    // If you want to additionally use the CameraX Lifecycle library
    implementation("androidx.camera:camera-lifecycle:${camerax_version}")
    // If you want to additionally use the CameraX VideoCapture library
    implementation("androidx.camera:camera-video:${camerax_version}")
    // If you want to additionally use the CameraX View class
    implementation("androidx.camera:camera-view:${camerax_version}")
    // If you want to additionally add CameraX ML Kit Vision Integration
    implementation("androidx.camera:camera-mlkit-vision:${camerax_version}")
    // If you want to additionally use the CameraX Extensions library
    implementation("androidx.camera:camera-extensions:${camerax_version}")

    implementation("com.google.accompanist:accompanist-permissions:0.19.0")
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
//    Gemini AI
    implementation(libs.generativeai)
//    Coil
    implementation(libs.coil.compose)
//    LoopJ Async
    implementation(libs.android.async.http)
//    Location services
    implementation(libs.play.services.location)
//    Google Maps SDK
    implementation(libs.maps.compose)
//    Maps SDK Utility
    implementation(libs.maps.compose.utils)
//    Maps SDK Widgets
    implementation(libs.maps.compose.widgets)
//    Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
//    Coroutine
    implementation(libs.kotlinx.coroutines.android)
//    LiveData
    implementation(libs.androidx.lifecycle.livedata.ktx)
//    ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.compose)
//    Coroutine
    implementation(libs.androidx.lifecycle.runtime.ktx)
//    Firebase
    implementation(libs.firebase.crashlytics.buildtools)
//    Gson
    implementation(libs.gson)
//    Splash Screen
    implementation(libs.androidx.core.splashscreen)

    implementation(libs.androidx.material.icons.extended.android)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.auth)

    ksp(libs.androidx.room.compiler)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)








    implementation(platform("com.google.firebase:firebase-bom:33.6.0"))
    implementation("com.google.firebase:firebase-analytics")
}

secrets {
    propertiesFileName = "local.properties"
    defaultPropertiesFileName = "secrets.properties"
}