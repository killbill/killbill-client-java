# killbill-client-java
![Maven Central](https://img.shields.io/maven-central/v/org.kill-bill.billing/killbill-client-java?color=blue&label=Maven%20Central)

Java client library for Kill Bill.

## Project Overview
Kill Bill is the leading Open-Source Subscription Billing & Payments Platform. For more information about the project, visit [Kill Bill](https://killbill.io/).

## Kill Bill Compatibility
| Client version | Kill Bill version |
| -------------: | ----------------: |
| 0.23.y         | 0.16.z            |
| 0.40.y         | 0.18.z            |
| 0.41.y         | 0.19.z            |
| 1.0.y          | 0.20.z            |
| 1.1.y          | 0.22.z            |
| 1.2.y          | 0.22.z            |
| 1.3.y          | 0.24.z            |

We've upgraded numerous dependencies in 1.2.x (required for Java 11 support).

## Prerequisites
This project requires Java (specific versions compatible with Kill Bill). Refer to the compatibility table for guidance.

## Installation
### Manual Installation
Add the library to your application:
```xml
<dependency>
    <groupId>org.kill-bill.billing</groupId>
    <artifactId>killbill-client-java</artifactId>
    <version>... release version ...</version>
</dependency>
```
Refer to the [Java client tutorial](https://docs.killbill.io/latest/java_client.html) and the sample [Java client application](https://github.com/killbill/killbill-client-java-example) for more details.

### Build Instructions
To build the library:
```sh
mvn clean install -DskipTests=true
```
This will produce several jars, including:
- `killbill-client-java-x.y.z-SNAPSHOT.jar` which contains the library only.
- `killbill-client-java-x.y.z-SNAPSHOT-jar-with-dependencies.jar` which is a self-contained jar (library and dependencies).

## External Documents
- [Java API Documentation](https://killbill.github.io/slate/?java)

## Help and Support
For FAQs and additional support, refer to the [Kill Bill documentation](https://docs.killbill.io/latest/java_client.html). Contribution including issues and pull requests are welcomed.

