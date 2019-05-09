killbill-client-java
====================

Java client library for Kill Bill.

Kill Bill compatibility
-----------------------

| Client version | Kill Bill version |
| -------------: | ----------------: |
| 0.23.y         | 0.16.z            |
| 0.40.y         | 0.18.z            |
| 0.41.y         | 0.19.z            |
| 1.0.y          | 0.20.z            |
| 1.1.y          | 0.21.z(dev branch)|

Usage
-----

The library is published in Maven Central:

    <dependency>
        <groupId>org.kill-bill.billing</groupId>
        <artifactId>killbill-client-java</artifactId>
        <version>1.0.6</version>
    </dependency>

The easiest way to get started is to look at some of our [integration tests](https://github.com/killbill/killbill/tree/master/profiles/killbill/src/test/java/org/killbill/billing/jaxrs), which use this library.

When sending a body, if you are unsure which parameters to pass, take a look at the [java api documentation](https://killbill.github.io/slate/?java#) code, which self-documents the required fields per call using preconditions.

Regarding error handling, if the server returns an error (400, 401, 409, 500, etc.), the code will throw `KillBillClientException`. If there is no response (204) or if an object cannot be found (404), the code will return null (for single objects) or an empty list (for collections of objects). Otherwise, you should never get null.

Build
-----

To build the library:

    mvn clean install

This will produce several jars:

* killbill-client-java-x.y.z-SNAPSHOT.jar which contains only the library
* killbill-client-java-x.y.z-SNAPSHOT-jar-with-dependencies.jar which is a self contained jar (library and dependencies)
