[![JitPack](https://img.shields.io/jitpack/v/github/adrielcafe/pufferdb.svg?style=flat-square)](https://jitpack.io/#adrielcafe/pufferdb) 
[![Codacy](https://img.shields.io/codacy/grade/a673b24ba23e4cd1bd1fdc9907aaafd2.svg?style=flat-square)](https://www.codacy.com/app/adriel_cafe/PufferDB) 
[![Android API](https://img.shields.io/badge/api-14%2B-brightgreen.svg?style=flat-square)](https://android-arsenal.com/api?level=14) 
[![License MIT](https://img.shields.io/github/license/adrielcafe/pufferdb.svg?style=flat-square)](https://opensource.org/licenses/MIT) 
[![ktlint](https://img.shields.io/badge/code%20style-%E2%9D%A4-FF4081.svg?style=flat-square)](https://ktlint.github.io/) 

# 🚧 WORK IN PROGRESS 🚧
This library is currently in pre-alpha and should **not** be used in production. Stable release coming very soon!

# ![logo](https://github.com/adrielcafe/pufferdb/blob/master/logo.png?raw=true) PufferDB

**PufferDB** is a :zap: key-value database powered by **P**rotocol B**uffer**s (aka [Protobuf](https://developers.google.com/protocol-buffers/)).

The intent of this library is to provide a efficient, reliable and Android **independent** storage. 

Why Android independent? The SharedPreferences and many great third-party libraries (like [Paper](https://github.com/pilgr/Paper/) and [MMKV](https://github.com/Tencent/MMKV/)) requires the Android Context to work (they're Android *dependents*). But if you are like me and want a **kotlin-only data module** (following the principles of [Clean Architecture](https://antonioleiva.com/clean-architecture-android/)), this library is for you!

### About Protobuf

Protocol Buffers are a language-neutral, platform-neutral extensible mechanism for serializing structured data. Compared to JSON, Protobuf files are [smaller and faster](https://auth0.com/blog/beating-json-performance-with-protobuf/) to read/write because it stores data in an [efficient binary format](https://developers.google.com/protocol-buffers/docs/encoding).

## Features
* Fast (benchmark coming soon™)
* Works on [Android and JVM](#platform-compatibility)
* [Simple API](#core)
* [Thread-safe](#thread-safe)
* Wrappers for [Coroutines](#coroutines) and [RxJava](#rxjava)

### Supported types
So far, PufferDB supports the following types:
- [x] Double
- [x] Float
- [x] Int
- [x] Long
- [x] Boolean
- [x] String

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

## Modules

### Core
As the name suggests, Core is a standalone module and all other modules depends on it.

It's API is similar to `SharedPreferences`:

```kotlin
val pufferFile = File("path/to/puffer/file")
val puffer = PufferDB.with(pufferFile)

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

But unlikely `SharedPreferences`, there's no `apply()` or `commit()`. Changes are saved every time a write operations happens.

PufferDB keeps an immutable and read-only memory cache for fast reads.

#### Threading
PufferDB uses the [`ReentrantReadWriteLock`](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/locks/ReentrantReadWriteLock.html) to ensure that read-write operations will run concurrently.

You *can* run the API methods in the Android Main Thread, but you *shouldn't* do that! Please, use one of the wrapper modules or extension functions instead.

### Android
The Android module contains an `AndroidPufferDB` helper class:
```kotlin
// Returns a default Puffer instance
val puffer = AndroidPufferDB.withDefault(context)

// Returns a File on Context.filesDir that should be used to create a Puffer instance
val pufferFile = AndroidPufferDB.getPufferFile(context, "my.db")
```

### Coroutines
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

### RxJava
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

Similar to the Coroutines module, the RxJava module also provides some useful built in extension functions that can be used with the Core module:
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
