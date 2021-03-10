# tronj

The TRON client library.

## How to use

### Gradle Setting

Add repo setting:

```groovy
repositories {
    maven {
        url  "https://dl.bintray.com/ki5fpl/tronj"
    }
}
```

Then add `abi` as dependency.

```groovy
dependencies {
    ....

    // protobuf & grpc
    implementation 'com.google.protobuf:protobuf-java:3.11.0'
    implementation 'io.grpc:grpc-stub:1.31.0'
    // tronj core libs
    implementation 'com.github.ki5fpl.tronj:abi:0.6.0'
    implementation 'com.github.ki5fpl.tronj:client:0.6.0'

    ....
}
```

### Maven Settings

Use maven repo setting from [Bintray](https://bintray.com/repo/downloadMavenRepoSettingsFile/downloadSettings?repoPath=%2Fki5fpl%2Ftronj).

```xml
<dependency>
  <groupId>com.github.ki5fpl.tronj</groupId>
  <artifactId>abi</artifactId>
  <version>0.5.0</version>
  <type>pom</type>
</dependency>
```

### Demo Code

Refer `demo` project.
