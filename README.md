[![JitPack](https://img.shields.io/jitpack/v/github/adrielcafe/pufferdb.svg?style=for-the-badge)](https://jitpack.io/#adrielcafe/pufferdb) 
[![Android API](https://img.shields.io/badge/api-14%2B-brightgreen.svg?style=for-the-badge)](https://android-arsenal.com/api?level=14) 
[![Bitrise](https://img.shields.io/bitrise/9cf30678e9638eba/master.svg?style=for-the-badge&token=-KZNny2tfEouRiyRtPQW7A)](https://app.bitrise.io/app/9cf30678e9638eba) 
[![Codecov](https://img.shields.io/codecov/c/github/adrielcafe/pufferdb/master.svg?style=for-the-badge)](https://codecov.io/gh/adrielcafe/pufferdb) 
[![Codacy](https://img.shields.io/codacy/grade/a673b24ba23e4cd1bd1fdc9907aaafd2.svg?style=for-the-badge)](https://www.codacy.com/app/adriel_cafe/pufferdb) 
[![kotlin](https://img.shields.io/github/languages/top/adrielcafe/pufferdb.svg?style=for-the-badge)](https://kotlinlang.org/) 
[![ktlint](https://img.shields.io/badge/code%20style-%E2%9D%A4-FF4081.svg?style=for-the-badge)](https://ktlint.github.io/) 
[![License MIT](https://img.shields.io/github/license/adrielcafe/pufferdb.svg?style=for-the-badge&color=yellow)](https://opensource.org/licenses/MIT) 

# 🚧 WORK IN PROGRESS 🚧
This library is currently in beta and should **not** be used in production. Stable release coming shortly!

# ![logo](https://github.com/adrielcafe/pufferdb/blob/master/logo.png?raw=true) PufferDB

**PufferDB** is a :zap: key-value database powered by **P**rotocol B**uffer**s (aka [Protobuf](https://developers.google.com/protocol-buffers/)).

The purpose of this library is to provide an efficient, reliable and **Android independent** storage. 

Why Android independent? The [SharedPreferences](https://developer.android.com/reference/android/content/SharedPreferences) and many great third-party libraries (like [Paper](https://github.com/pilgr/Paper/) and [MMKV](https://github.com/Tencent/MMKV/)) requires the Android Context to work. But if you are like me and want a **kotlin-only data module** (following the principles of [Clean Architecture](https://antonioleiva.com/clean-architecture-android/)), this library is for you!

### About Protobuf

Protocol Buffers are a language-neutral, platform-neutral extensible mechanism for serializing structured data. Compared to JSON, Protobuf files are [smaller and faster](https://auth0.com/blog/beating-json-performance-with-protobuf/) to read/write because the data is stored in an [efficient binary format](https://developers.google.com/protocol-buffers/docs/encoding).

## Features
* [Fast](#benchmark)
* Works on [Android and JVM](#platform-compatibility)
* [Simple API](#core)
* [Thread-safe](#threading)
* Wrappers for [Coroutines](#coroutines) and [RxJava](#rxjava)

### Supported types
So far, PufferDB supports the following types:
- [x] `Double` and `List<Double>`
- [x] `Float` and `List<Float>`
- [x] `Int` and `List<Int>`
- [x] `Long` and `List<Long>`
- [x] `Boolean` and `List<Boolean>`
- [x] `String` and `List<String>`

## Getting Started

### Import to your project
1. Add the JitPack repository in your root build.gradle at the end of repositories:
```gradle
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

2. Next, add the desired dependencies to your module:
```gradle
dependencies {
    // Core library
    implementation "com.github.adrielcafe.pufferdb:core:$currentVersion"

    // Android helper
    implementation "com.github.adrielcafe.pufferdb:android:$currentVersion"

    // Coroutines wrapper
    implementation "com.github.adrielcafe.pufferdb:coroutines:$currentVersion"

    // RxJava wrapper
    implementation "com.github.adrielcafe.pufferdb:rxjava:$currentVersion"
}
```
Current version: [![JitPack](https://img.shields.io/jitpack/v/github/adrielcafe/pufferdb.svg?style=flat-square)](https://jitpack.io/#adrielcafe/pufferdb)

### Platform compatibility

|         | `core` | `android` | `coroutines` | `rxjava` |
|---------|--------|-----------|--------------|----------|
| Android | ✓      | ✓        | ✓            | ✓       |
| JVM     | ✓      |           | ✓           | ✓        |

## Core
As the name suggests, Core is a standalone module and all other modules depends on it.

To create a new `Puffer` instance you must tell which file to use. 
```kotlin
val pufferFile = File("path/to/puffer/file")
val puffer = PufferDB.with(pufferFile)
```

If you are on Android, I recommend to use the [Context.filesDir](https://developer.android.com/training/data-storage/files#WriteFileInternal) as the parent folder. If you want to save in the external storage remember to [ask for write permission](https://developer.android.com/training/data-storage/files#ExternalStoragePermissions) first.

Its API is similar to `SharedPreferences`:
```kotlin
puffer.apply {
    val myValue = get<String>("myKey")
    val myValueWithDefault = get("myKey", "defaultValue")
    
    put("myOtherKey", 123)

    getKeys().forEach { key ->
        // ...
    }

    if(contains("myKey")){
        // ...
    }

    remove("myOtherKey")

    removeAll()
}
```

But unlike `SharedPreferences`, there's no `apply()` or `commit()`. Changes are saved asynchronously every time a write operation (`put()`, `remove()` and `removeAll()`) happens.

### Threading
PufferDB uses a [`ConcurrentHashMap`](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ConcurrentHashMap.html) to manage a thread-safe in-memory cache for fast read and write operations.

Changes are saved asynchronously with the help of a [Conflated Channel](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.channels/-channel/index.html) (to save the most recent state in a race condition) and [Mutex](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.sync/-mutex/index.html) lock (to prevent simultaneous writes).

It is possible to run the API methods on the Android Main Thread, but you should *avoid that*. You can use one of the wrapper modules or built in extension functions for that.

## Android
The Android module contains an `AndroidPufferDB` helper class:
```kotlin
// Returns a default Puffer instance, the file is located on Context.filesDir
val puffer = AndroidPufferDB.withDefault(context)

// Returns a File on Context.filesDir that should be used to create a Puffer instance
val pufferFile = AndroidPufferDB.getInternalFile(context, "my.db")
```

## Coroutines
The Coroutines module contains a `CoroutinePufferDB` wrapper class and some useful extension functions:
```kotlin
val pufferFile = File("path/to/puffer/file")
val puffer = CoroutinePufferDB.with(pufferFile)

puffer.apply {
    // All methods are suspend functions that runs on Dispatchers.IO context
    launch {
        val myValue = get<String>("myKey")
        val myValueWithDefault = get("myKey", "defaultValue")
        
        put("myOtherKey", 123)

        getKeys().forEach { key ->
            // ...
        }

        if(contains("myKey")){
            // ...
        }

        remove("myOtherKey")

        removeAll()
    }
}
```

If you don't want to use this wrapper class, there's some built in extension functions that can be used with the Core module:
```kotlin
val pufferFile = File("path/to/puffer/file")
val puffer = PufferDB.with(pufferFile) // <- Note that we're using the Core PufferDB

puffer.apply {
    launch {
        val myValue = getSuspend<String>("myKey")

        val myValue = getAsync<String>("myKey").await()
        
        // You can use your custom coroutine context...
        putSuspend("myOtherKey", 123, Dispatchers.Unconfined)

        // ... And your custom coroutine scope
        putAsync("myOtherKey", 123, myActivityScope).await()
    }
}
```

## RxJava
The RxJava module contains a `RxPufferDB` wrapper class and some useful extension functions:
```kotlin
val pufferFile = File("path/to/puffer/file")
val puffer = RxPufferDB.with(pufferFile)

puffer.apply {
    // Some methods returns Single<T>...
    get<String>("myKey") // OR get("myKey", "defaultValue")
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { myValue ->
            // ...
        }

    // ... And others returns Completable
    put("myOtherKey", 123)
        // ...
        .subscribe {
            // ...
        }

    getKeys()
        // ...
        .subscribe { keys ->
            // ...
        }

    contains("myKey")
        // ...
        .subscribe { contains ->
            // ...
        }

    remove("myOtherKey")
        // ...
        .subscribe {
            // ...
        }

    removeAll()
        // ...
        .subscribe {
            // ...
        }
}
```

Like the Coroutines module, the RxJava module also provides some useful built in extension functions that can be used with the Core module:
```kotlin
val pufferFile = File("path/to/puffer/file")
val puffer = PufferDB.with(pufferFile) // <- Note that we're using the Core PufferDB

puffer.apply {
    val myValue = getSingle<String>("myKey").blockingGet()

    putCompletable("myOtherKey", 123).blockingAwait()

    getKeysObservable().blockingSubscribe { keys ->
        // ...
    }
}
```

## Benchmark

|  | Write 1k strings (ms) | Read 1k strings (ms) |
|-------------------|-----------------------|----------------------|
| **PufferDB** | **45** | **6** |
| SharedPreferences | 268 | 7 |
| MMKV | 15 | 9 |
| Paper | 648 | 183 |
| Hawk | 11116 | 211 |

*Tested on Moto Z2 Plus*

You can run the [Benchmark](https://github.com/adrielcafe/PufferDB/blob/master/sample/src/main/kotlin/cafe/adriel/pufferdb/sample/Benchmark.kt) through the sample app.