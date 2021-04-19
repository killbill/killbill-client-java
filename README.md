# killbill-client-java
![Maven Central](https://img.shields.io/maven-central/v/org.kill-bill.billing/killbill-client-java?color=blue&label=Maven%20Central)

Java client library for Kill Bill.

## Kill Bill compatibility

| Client version | Kill Bill version |
| -------------: | ----------------: |
| 0.23.y         | 0.16.z            |
| 0.40.y         | 0.18.z            |
| 0.41.y         | 0.19.z            |
| 1.0.y          | 0.20.z            |
| 1.1.y          | 0.22.z            |
| 1.2.y          | 0.22.z            |

We've upgraded numerous dependencies in 1.2.x (required for Java 11 support).

## Usage

Add the library to your application:

```xml
<dependency>
    <groupId>org.kill-bill.billing</groupId>
    <artifactId>killbill-client-java</artifactId>
    <version>... release version ...</version>
</dependency>
```

The easiest way to get started is to look at our [Java client tutorial](https://docs.killbill.io/latest/java_client.html). We also provide a sample [Java client application](https://github.com/killbill/killbill-client-java-example). You can clone this project in order to get started.

When sending a body, if you are unsure which parameters to pass, take a look at the [Java api documentation](https://killbill.github.io/slate/?java#).

## Build

To build the library:

```
mvn clean install -DskipTests=true
```

This will produce several jars, including:

* killbill-client-java-x.y.z-SNAPSHOT.jar which contains the library only
* killbill-client-java-x.y.z-SNAPSHOT-jar-with-dependencies.jar which is a self-contained jar (library and dependencies)

## About

Kill Bill is the leading Open-Source Subscription Billing & Payments Platform. For more information about the project, go to https://killbill.io/.
