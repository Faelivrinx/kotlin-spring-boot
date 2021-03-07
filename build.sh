#!/bin/bash

echo 'Building the project has started...'

cd task-service
./mvnw clean install -P docker

cd ../reports-service
./mvnw clean install -P docker

echo 'Build finished'

