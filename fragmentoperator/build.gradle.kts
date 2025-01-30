plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("maven-publish")
}

group = "com.github.hm1113"
version = "1.2.4"

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

// ✅ Register sources & Javadoc tasks
val androidSourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(android.sourceSets["main"].java.srcDirs)
}

val androidJavadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
}

// ✅ Ensure dependencies are resolved correctly
publishing {
    publications {
        create<MavenPublication>("release") {
            afterEvaluate {
                from(components["release"])

                artifact(androidSourcesJar.get()) // ✅ Use .get() to reference task correctly
                artifact(androidJavadocJar.get())

                groupId = "com.github.hm1113"
                artifactId = "FragmentOperator"
                version = "1.2.4"

                pom {
                    name.set("FragmentOperator")
                    description.set("A simple fragment operator library for Android.")
                    url.set("https://github.com/hm1113/FragmentOperator")

                    licenses {
                        license {
                            name.set("MIT License")
                            url.set("https://opensource.org/licenses/MIT")
                        }
                    }

                    developers {
                        developer {
                            id.set("hm1113")
                            name.set("Hem")
                        }
                    }

                    scm {
                        connection.set("scm:git:github.com/hm1113/FragmentOperator.git")
                        developerConnection.set("scm:git:ssh://github.com/hm1113/FragmentOperator.git")
                        url.set("https://github.com/hm1113/FragmentOperator")
                    }
                }
            }
        }
    }
}

// ✅ Ensure correct task execution order
tasks.named("generateMetadataFileForReleasePublication") {
    dependsOn(androidSourcesJar)
}

