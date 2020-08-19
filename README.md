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

The easiest way to get started is to look at some of our [integration tests](https://github.com/killbill/killbill/tree/master/profiles/killbill/src/test/java/org/killbill/billing/jaxrs), which use this library.

When sending a body, if you are unsure which parameters to pass, take a look at the [Java api documentation](https://killbill.github.io/slate/?java#).

Regarding error handling, if the server returns an error (400, 401, 409, 500, etc.), the code will throw `KillBillClientException`. If there is no response (204) or if an object cannot be found (404), the code will return null (for single objects) or an empty list (for collections of objects). Otherwise, you should never get null.

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
