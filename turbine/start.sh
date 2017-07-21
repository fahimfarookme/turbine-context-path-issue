#!/bin/bash

mvn clean package
cd target
java -Dport=8896 -Dhostname=primary-host    -Dserver.list=http://primary-host:7776/eureka -jar turbine-0.0.1-SNAPSHOT.jar &
