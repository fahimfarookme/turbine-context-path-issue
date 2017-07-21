#!/bin/bash

# mvn clean package
cd target
java -Dport=8886 -Dhostname=primary-host    -Dserver.list=http://primary-host:7776/eureka -jar hystrix-eureka-client-0.0.1-SNAPSHOT.jar &