# kotlin-spring-boot

#Getting started

## :pushpin: Pre-requirements
- docker
- docker-compose

## :hammer: Build images
```shell
cd ./client/
mvn clean install -P docker
cd ../manager/
mvn clean install -P docker
```

## :rocket: Running project
The easiest way to start project is to use `docker-compose.yml` where is configuration for services and ELK stack. 
```shell
docker-compose up
```

## First request
```shell
curl http://localhost:8080/
```

