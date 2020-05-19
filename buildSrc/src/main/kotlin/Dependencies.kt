@file:Suppress("Unused", "MayBeConstant", "MemberVisibilityCanBePrivate")

internal object Version {
    const val GRADLE_ANDROID = "3.6.3"
    const val GRADLE_PROTOBUF = "0.8.12"
    const val GRADLE_DETEKT = "1.9.1"
    const val GRADLE_KTLINT = "9.2.1"
    const val GRADLE_JACOCO = "0.16.0"
    const val GRADLE_VERSIONS = "0.28.0"
    const val GRADLE_MAVEN = "2.1"

    const val KOTLIN = "1.3.72"
    const val COROUTINES = "1.3.6"

    const val RXJAVA = "3.0.3"
    const val RXANDROID = "3.0.0"

    const val ANDROIDX_PREFERENCE = "1.1.1"
    const val MMKV = "1.1.1"
    const val PAPER = "2.7.1"
    const val HAWK = "2.0.1"
    const val BINARY_PREFS = "1.0.1"

    const val APP_COMPAT = "1.1.0"

    const val PROTOBUF_LITE = "3.0.1"
    const val PROTOC = "3.12.0"
    const val PROTOC_LITE = "3.0.0"

    const val TEST_SPEK = "2.0.9"
    const val TEST_STRIKT = "0.25.0"
    const val TEST_MOCKK = "1.10.0"
}

object ProjectLib {
    const val ANDROID = "com.android.tools.build:gradle:${Version.GRADLE_ANDROID}"
    const val KOTLIN = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Version.KOTLIN}"
    const val PROTOBUF = "com.google.protobuf:protobuf-gradle-plugin:${Version.GRADLE_PROTOBUF}"
    const val DETEKT = "io.gitlab.arturbosch.detekt:detekt-gradle-plugin:${Version.GRADLE_DETEKT}"
    const val KTLINT = "org.jlleitschuh.gradle:ktlint-gradle:${Version.GRADLE_KTLINT}"
    const val JACOCO = "com.vanniktech:gradle-android-junit-jacoco-plugin:${Version.GRADLE_JACOCO}"
    const val VERSIONS = "com.github.ben-manes:gradle-versions-plugin:${Version.GRADLE_VERSIONS}"
    const val MAVEN = "com.github.dcendents:android-maven-gradle-plugin:${Version.GRADLE_MAVEN}"

    val all = setOf(ANDROID, KOTLIN, PROTOBUF, DETEKT, KTLINT, JACOCO, VERSIONS, MAVEN)
}

object ModuleLib {
    const val KOTLIN = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Version.KOTLIN}"
    const val COROUTINES_CORE = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Version.COROUTINES}"
    const val COROUTINES_ANDROID = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Version.COROUTINES}"

    const val PROTOC = "com.google.protobuf:protoc:${Version.PROTOC}"
    const val PROTOC_LITE = "com.google.protobuf:protoc-gen-javalite:${Version.PROTOC_LITE}"
    const val PROTOBUF_LITE = "com.google.protobuf:protobuf-lite:${Version.PROTOBUF_LITE}"

    const val RXJAVA = "io.reactivex.rxjava3:rxjava:${Version.RXJAVA}"
    const val RXANDROID = "io.reactivex.rxjava3:rxandroid:${Version.RXANDROID}"

    const val ANDROIDX_PREFERENCE = "androidx.preference:preference-ktx:${Version.ANDROIDX_PREFERENCE}"
    const val MMKV = "com.tencent:mmkv:${Version.MMKV}"
    const val PAPER = "io.paperdb:paperdb:${Version.PAPER}"
    const val HAWK = "com.orhanobut:hawk:${Version.HAWK}"
    const val BINARY_PREFS = "com.github.yandextaxitech:binaryprefs:${Version.BINARY_PREFS}"

    const val APP_COMPAT = "androidx.appcompat:appcompat:${Version.APP_COMPAT}"
}

object TestLib {
    const val SPEK_DSL = "org.spekframework.spek2:spek-dsl-jvm:${Version.TEST_SPEK}"
    const val SPEK_RUNNER = "org.spekframework.spek2:spek-runner-junit5:${Version.TEST_SPEK}"
    const val STRIKT = "io.strikt:strikt-core:${Version.TEST_STRIKT}"
    const val MOCKK = "io.mockk:mockk:${Version.TEST_MOCKK}"
    const val COROUTINES = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Version.COROUTINES}"

    val all = setOf(SPEK_DSL, SPEK_RUNNER, STRIKT, MOCKK, COROUTINES)
}
