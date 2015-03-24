#!/bin/bash

cd employeemanagement-dao
mvn clean install
cd ..
cd employeemanagement-service
mvn clean install
cd ..
cd employeemanagement
mvn clean install
cd ..
