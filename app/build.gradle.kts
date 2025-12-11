plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.imageclassifiertutorial"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.imageclassifiertutorial"
        minSdk = 30
        targetSdk = 36
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
    // TensorFlow Lite runtime
    implementation("org.tensorflow:tensorflow-lite:2.14.0")
    {
        exclude("com.google.ai.edge.litert")

    }
// TensorFlow Lite support library for model handling
    implementation("org.tensorflow:tensorflow-lite-support:0.5.0")
    {
        exclude("com.google.ai.edge.litert")
    }
// Glide for image loading
    implementation("com.github.bumptech.glide:glide:4.12.0")

    configurations {
        all {
            exclude("com.google.ai.edge.litert")
        }
    }
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}