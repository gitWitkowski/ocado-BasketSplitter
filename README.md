# Witkowski Maciej
This is my solution to the task for Ocado Technology Internship recruitment process.

## Used technologies
- Java 17
- Maven
- JUnit 5
- [JSON in Java [package org.json]](https://mvnrepository.com/artifact/org.json/json)
- Apache Maven Assembly Plugin

## Approach to the solution
My idea was to first, based on input items, list all available delivery methods and count the number of their occurrence. The next step was to sort available methods in descending order. Then, starting from the most popular delivery method, take all items from the basket with that delivery method and assign them to it. After that, repeat this procedure with the second most popular delivery method and so on, until there are no more items in the basket without the delivery method assigned.

## How to build the project
1. Enter main directory
```shell
cd ocado-BasketSplitter
```
2. Clean and build the project using Maven
```shell
mvn clean package
```
3. Your .jar file called `Maciej_Witkowski_Java_Krakow-jar-with-dependencies.jar` will be located in `target` directory
