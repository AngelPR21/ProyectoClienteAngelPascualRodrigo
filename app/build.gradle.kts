plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "es.ieslavereda.baseoficios"
    compileSdk = 35

    defaultConfig {
        applicationId = "es.ieslavereda.baseoficios"
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    // Libreria para la manipulacion de json
    implementation("com.google.code.gson:gson:2.11.0")
    // Libreria para las llamadas a la API
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    // Libreria para el manejo de imagenes
    implementation("com.squareup.picasso:picasso:2.8")
    // Libreria para el manejo de imagenes
    implementation("com.android.volley:volley:1.2.1")
}