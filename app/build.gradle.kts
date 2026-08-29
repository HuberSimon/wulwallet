plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)

    id("kotlin-kapt")
}

android {

    namespace = "com.example.wulwallet"

    compileSdk = 35

    defaultConfig {

        applicationId = "com.example.wulwallet"

        minSdk = 24

        targetSdk = 35

        versionCode = 1

        versionName = "1.0"

        testInstrumentationRunner =
            "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {

        release {

            isMinifyEnabled = false

            proguardFiles(
                getDefaultProguardFile(
                    "proguard-android-optimize.txt"
                ),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {

        sourceCompatibility =
            JavaVersion.VERSION_17

        targetCompatibility =
            JavaVersion.VERSION_17
    }

    kotlinOptions {

        jvmTarget = "17"
    }

    buildFeatures {

        compose = true
    }

    // ============================================================
    // WICHTIG:
    // Kotlin 1.9.0
    // Compose Compiler 1.5.1
    // ============================================================

    composeOptions {

        kotlinCompilerExtensionVersion = "1.5.1"
    }

    packaging {

        resources {

            excludes +=
                "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    // ============================================================
    // CORE
    // ============================================================

    implementation(
        libs.androidx.core.ktx
    )

    implementation(
        libs.androidx.lifecycle.runtime.ktx
    )

    implementation(
        libs.androidx.activity.compose
    )


    // ============================================================
    // COMPOSE BOM
    // ============================================================

    implementation(
        platform(
            libs.androidx.compose.bom
        )
    )


    // ============================================================
    // COMPOSE UI
    // ============================================================

    implementation(
        libs.androidx.ui
    )

    implementation(
        libs.androidx.ui.graphics
    )

    implementation(
        libs.androidx.ui.tooling.preview
    )

    implementation(
        libs.androidx.material3
    )

    implementation(
        libs.androidx.material.icons.extended
    )


    // ============================================================
    // NAVIGATION
    // ============================================================

    implementation(
        libs.androidx.navigation.compose
    )


    // ============================================================
    // CONSTRAINT LAYOUT
    // ============================================================

    implementation(
        libs.androidx.constraintlayout
    )


    // ============================================================
    // ACCOMPANIST
    // ============================================================

    implementation(
        libs.accompanist.pager
    )

    implementation(
        libs.accompanist.pager.indicators
    )


    // ============================================================
    // GOOGLE MAPS
    // ============================================================

    implementation(
        libs.play.services.maps
    )


    // ============================================================
    // ROOM
    // ============================================================

    implementation(
        libs.androidx.room.runtime
    )

    implementation(
        libs.androidx.room.ktx
    )

    kapt(
        libs.androidx.room.compiler
    )


    // ============================================================
    // COROUTINES
    // ============================================================

    implementation(
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3"
    )


    // ============================================================
    // TEST
    // ============================================================

    testImplementation(
        libs.junit
    )

    androidTestImplementation(
        libs.androidx.junit
    )

    androidTestImplementation(
        libs.androidx.espresso.core
    )

    androidTestImplementation(
        platform(
            libs.androidx.compose.bom
        )
    )

    androidTestImplementation(
        libs.androidx.ui.test.junit4
    )


    // ============================================================
    // DEBUG
    // ============================================================

    debugImplementation(
        libs.androidx.ui.tooling
    )

    debugImplementation(
        libs.androidx.ui.test.manifest
    )
}