# Flowable Spring Boot Demo Project

## Project Structure

```dotenv
├───src
│   ├───main
│   │   ├───java
│   │   │   └───com
│   │   │       └───epam
│   │   │           └───demo
│   │   │               ├───config
│   │   │               ├───controller
│   │   │               ├───handler
│   │   │               └───service
│   │   └───resources
│   │       └───processes
│   └───test
│       └───java
│           └───com
│               └───epam
│                   └───demo

```
## Version
* SpringBoot 2.6.6 
* Flowable 6.7.2

## Features
1. Build an expense process by Flowable
   1. The process file is in src/main/resources/processes
   2. You can generate BPM XML by downloading the [Flowable Applications](http://www.flowable.org/downloads.html)
   3. How to design a BPM by Flowable Applications: [BPMN User Guide](https://www.flowable.com/open-source/docs/bpmn/ch02-GettingStarted)
2. Download the BPM XML file and copy it into src/main/resources/processes folder.
3. Flowable is using the Liquibase to generate required table.
   1. You need config your mysql in application.yml file
   ```
   spring:
    datasource:
        driverClassName: com.mysql.cj.jdbc.Driver
        password: hewanyu1114
        url: jdbc:mysql://127.0.0.1:3306/flowable?characterEncoding=UTF-8&serverTimezone=UTC
        username: root
    flowable:
    # Close flowable async job
    async-executor-activate: false
   ```
4. Add proxy assignee role
   ```
   BossTaskHandler
   ManagerTaskHandler
   ```
5. Add major handler service
```
ExpenseService
```
6. Add rest api for each link of BPM process
```
ExpenseController
```
7. You can request the rest api to start,approved,rejected and stop the process.

## How to config Followable Applications.

1. Install JDK8.x or later.
2. Downloading Flowable6.7.2 [Flowable Applications](http://www.flowable.org/downloads.html)
   1. Get in the flowable6.7.2 folder and find the 'wars' folder.
   2. Copy the 'flowable-ui.war' to 'Tomcat 10.0/webapps'
3. Start 'flowable-ui.war'
```shell
java -jar flowable-ui.war
```
4. Open the URL: http://localhost:8080/flowable-ui/
5. The default username and password is "admin:test"