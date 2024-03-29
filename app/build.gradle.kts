plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
    kotlin("kapt")
}

android {
    namespace = "app.divarinterview.android"
    compileSdk = 34

    defaultConfig {
        applicationId = "app.divarinterview.android"
        minSdk = 21
        targetSdk = 34
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
    buildFeatures {
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    val splashscreenVersion = "1.0.1"
    val hiltVersion = "2.45"
    val navVersion = "2.7.6"
    val gsonVersion = "2.10.1"
    val retrofitVersion = "2.9.0"
    val coroutineVersion = "1.7.3"
    val eventbusVersion = "3.3.1"
    val locationVersion = "21.0.1"
    val frescoVersion = "3.1.3"
    val epoxyVersion = "5.1.3"
    val recyclerviewVersion = "1.3.2"
    val swiperefreshVersion = "1.1.0"
    val roomVersion = "2.6.1"
    val pagingVersion = "3.2.1"

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Recyclerview
    implementation("androidx.recyclerview:recyclerview:$recyclerviewVersion")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:$swiperefreshVersion")

    // Splash Screen
    implementation("androidx.core:core-splashscreen:$splashscreenVersion")

    // Hilt
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    kapt("com.google.dagger:hilt-android-compiler:$hiltVersion")

    // Navigation component
    implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navVersion")

    // Network and Retrofit
    implementation("com.google.code.gson:gson:$gsonVersion")
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")

    // Coroutine
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutineVersion")

    // Eventbus
    implementation("org.greenrobot:eventbus:$eventbusVersion")

    // Location Services
    implementation("com.google.android.gms:play-services-location:$locationVersion")

    // Fresco and Webp file support
    implementation("com.facebook.fresco:fresco:$frescoVersion")

    // Epoxy
    implementation("com.airbnb.android:epoxy:$epoxyVersion")
    ksp("com.airbnb.android:epoxy-processor:$epoxyVersion")
    implementation("com.airbnb.android:epoxy-paging3:$epoxyVersion")

    //Paging
    implementation("androidx.paging:paging-runtime-ktx:$pagingVersion")

    // Room
    implementation("androidx.room:room-ktx:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-paging:$roomVersion")

    // Slider
    implementation("androidx.viewpager2:viewpager2:1.0.0")
    implementation("com.tbuonomo:dotsindicator:5.0")

}