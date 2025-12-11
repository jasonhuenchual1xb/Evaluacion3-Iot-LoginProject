plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    // id("com.android.application")  <-- ESTA LINEA SOBRABA, YA ESTÁ EN EL ALIAS
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.loginproject"
    compileSdk = 34 // He cambiado "release(36)" a 34 o 35, que es lo estándar. Si te da error, pon 34.

    defaultConfig {
        applicationId = "com.example.loginproject"
        minSdk = 24
        targetSdk = 34 // Asegúrate de que coincida con compileSdk
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
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // --- FIREBASE ---
    // Plataforma BOM (Solo una vez y la versión más reciente)
    implementation(platform("com.google.firebase:firebase-bom:34.6.0"))

    // Librerías de Firebase (Sin versiones, el BOM las controla)
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")

    // Google Sign-In (Necesario para el Login con Google)
    implementation("com.google.android.gms:play-services-auth:21.0.0")

    // Imágenes
    implementation("com.github.bumptech.glide:glide:4.16.0")

    // ... (dentro del bloque dependencies { ... })

    // Imágenes
    implementation("com.github.bumptech.glide:glide:4.16.0")

    // ----------------------------------------------------
    // DEPENDENCIAS DE LA EVALUACIÓN 4 (EV4)
    // ----------------------------------------------------

    // OSMDroid (Mapa Gratuito)
    implementation("org.osmdroid:osmdroid-android:6.1.18")

    // Google Location Services (para GPS, usado para el rastreo en movimiento)
    implementation("com.google.android.gms:play-services-location:21.0.1")

    // Coroutines (para la simulación asíncrona del sensor)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
}
