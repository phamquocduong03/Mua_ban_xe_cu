plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")  // Plugin Google services
}

android {
    namespace = "com.example.mua_ban_xe_cu"

    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.mua_ban_xe_cu"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {

    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.dropbox.core:dropbox-core-sdk:7.0.0")
    implementation("com.google.firebase:firebase-storage:20.0.1")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    // Firebase dependencies
    implementation("com.google.firebase:firebase-auth:21.0.1")
    implementation("com.google.firebase:firebase-firestore:24.0.0")
    implementation("com.google.firebase:firebase-database:20.0.3") // If using Realtime Database

    // Room dependencies
    implementation("androidx.room:room-runtime:2.5.0")  // Room runtime library
    annotationProcessor("androidx.room:room-compiler:2.5.0")  // Room compiler for annotation processing

    // Glide for image loading
    implementation("com.github.bumptech.glide:glide:4.15.1")
    annotationProcessor("com.github.bumptech.glide:compiler:4.15.1")

    // AndroidX dependencies
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.3")

    // Kotlin support for Android
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.8.20")

    implementation(platform("com.google.firebase:firebase-bom:33.7.0"))
    implementation("com.google.firebase:firebase-analytics")
}

// Apply the Google services plugin
apply(plugin = "com.google.gms.google-services")
