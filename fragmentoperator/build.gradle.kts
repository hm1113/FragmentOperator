plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("maven-publish")
}

group = "com.github.hm1113"
version = "1.2.2"

android {
    namespace = "com.tubitv.fragmentoperator"
    compileSdk = 35

    buildFeatures {
        buildConfig = true // Enable BuildConfig generation
    }

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        debug {
            buildConfigField("boolean", "APP_DEBUG", "true")
        }

        release {
            buildConfigField("boolean", "APP_DEBUG", "false")

            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

publishing {
    publications {
        create<MavenPublication>("release") {
            afterEvaluate { // Ensures all components are available
                from(components["release"]) // ✅ Use "release" for Android libraries
            }
            groupId = "com.github.hm1113"
            artifactId = "FragmentOperator"
            version = "1.2.2"
        }
    }
}