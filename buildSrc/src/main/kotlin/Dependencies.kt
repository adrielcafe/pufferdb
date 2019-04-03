object Version {
    internal const val GRADLE_ANDROID = "3.4.0-rc03"
    internal const val GRADLE_PROTOBUF = "0.8.8"
    internal const val GRADLE_DETEKT = "1.0.0-RC14"
    internal const val GRADLE_KTLINT = "7.2.1"
    internal const val GRADLE_JACOCO = "0.13.0"
    internal const val GRADLE_VERSIONS = "0.21.0"
    internal const val GRADLE_ANDROID_MAVEN = "2.1"

    internal const val KOTLIN = "1.3.21"
    internal const val COROUTINES = "1.1.1"

    internal const val RXJAVA = "2.2.8"
    internal const val RXANDROID = "2.1.1"

    internal const val APP_COMPAT = "1.1.0-alpha03"

    internal const val PROTOBUF_LITE = "3.0.1"
    internal const val PROTOC = "3.7.1"
    internal const val PROTOC_LITE = "3.0.0"

    internal const val TEST_JUNIT = "4.12"
    internal const val TEST_ESPRESSO = "3.1.1"
    internal const val TEST_RUNNER = "1.1.1"
    internal const val TEST_SPEK = "2.0.1"
    internal const val TEST_STRIKT = "0.19.7"
}

object ProjectLib {
    const val ANDROID = "com.android.tools.build:gradle:${Version.GRADLE_ANDROID}"
    const val KOTLIN = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Version.KOTLIN}"
    const val PROTOBUF = "com.google.protobuf:protobuf-gradle-plugin:${Version.GRADLE_PROTOBUF}"
    const val DETEKT = "io.gitlab.arturbosch.detekt:detekt-gradle-plugin:${Version.GRADLE_DETEKT}"
    const val KTLINT = "org.jlleitschuh.gradle:ktlint-gradle:${Version.GRADLE_KTLINT}"
    const val JACOCO = "com.vanniktech:gradle-android-junit-jacoco-plugin:${Version.GRADLE_JACOCO}"
    const val VERSIONS = "com.github.ben-manes:gradle-versions-plugin:${Version.GRADLE_VERSIONS}"
    const val ANDROID_MAVEN = "com.github.dcendents:android-maven-gradle-plugin:${Version.GRADLE_ANDROID_MAVEN}"
}

object ModuleLib {
    const val KOTLIN = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Version.KOTLIN}"
    const val COROUTINES = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Version.COROUTINES}"

    const val RXJAVA = "io.reactivex.rxjava2:rxjava:${Version.RXJAVA}"
    const val RXANDROID = "io.reactivex.rxjava2:rxandroid:${Version.RXANDROID}"

    const val APP_COMPAT = "androidx.appcompat:appcompat:${Version.APP_COMPAT}"

    const val PROTOBUF_LITE = "com.google.protobuf:protobuf-lite:${Version.PROTOBUF_LITE}"
    const val PROTOC = "com.google.protobuf:protoc:${Version.PROTOC}"
    const val PROTOC_LITE = "com.google.protobuf:protoc-gen-javalite:${Version.PROTOC_LITE}"
}

object TestLib {
    const val JUNIT = "junit:junit:${Version.TEST_JUNIT}"
    const val ESPRESSO = "androidx.test.espresso:espresso-core:${Version.TEST_ESPRESSO}"
    const val RUNNER = "androidx.test:runner:${Version.TEST_RUNNER}"
    const val SPEK_DSL = "org.spekframework.spek2:spek-dsl-jvm:${Version.TEST_SPEK}"
    const val SPEK_RUNNER = "org.spekframework.spek2:spek-runner-junit5:${Version.TEST_SPEK}"
    const val STRIKT = "io.strikt:strikt-core:${Version.TEST_STRIKT}"
}
