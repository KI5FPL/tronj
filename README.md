# tronj

The TRON client library.

**NOTE: This project is adopted by TRON Foudation and moved to** https://github.com/tronprotocol/tronj .

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

    implementation 'com.github.ki5fpl.tronj:abi:0.4.0'
    implementation 'com.github.ki5fpl.tronj:client:0.4.0'

    ....
}
```

### Maven Settings

Use maven repo setting from [Bintray](https://bintray.com/repo/downloadMavenRepoSettingsFile/downloadSettings?repoPath=%2Fki5fpl%2Ftronj).

```xml
<dependency>
  <groupId>com.github.ki5fpl.tronj</groupId>
  <artifactId>abi</artifactId>
  <version>0.4.0</version>
  <type>pom</type>
</dependency>
```

### Demo Code

Refer `demo` project.

```java
/*
import java.math.BigInteger;
import java.util.*;

import com.github.ki5fpl.tronj.abi.FunctionEncoder;
import com.github.ki5fpl.tronj.abi.datatypes.*;
import com.github.ki5fpl.tronj.abi.datatypes.generated.Bytes10;
import com.github.ki5fpl.tronj.abi.datatypes.generated.Uint256;
import com.github.ki5fpl.tronj.abi.datatypes.generated.Uint32;
*/


// Function(name, input, output)
Function function =
        new Function(
                "sam",
                Arrays.asList(
                        new DynamicBytes("dave".getBytes()),
                        new Bool(true),
                        new Address("T9yKC9LCoVvmhaFxKcdK9iL18TUWtyFtjh"),
                        new DynamicArray<>(
                                new Uint(BigInteger.ONE),
                                new Uint(BigInteger.valueOf(2)),
                                new Uint(BigInteger.valueOf(3)))),
                Collections.emptyList());
String encodedHex = FunctionEncoder.encode(function);

/*
465c405b
0000000000000000000000000000000000000000000000000000000000000080
0000000000000000000000000000000000000000000000000000000000000001
00000000000000000000000000052b08330e05d731e38c856c1043288f7d9744
00000000000000000000000000000000000000000000000000000000000000c0
0000000000000000000000000000000000000000000000000000000000000004
6461766500000000000000000000000000000000000000000000000000000000
0000000000000000000000000000000000000000000000000000000000000003
0000000000000000000000000000000000000000000000000000000000000001
0000000000000000000000000000000000000000000000000000000000000002
0000000000000000000000000000000000000000000000000000000000000003
*/
```
