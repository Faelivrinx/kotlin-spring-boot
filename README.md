# kotlin-spring-boot

#Getting started

## :pushpin: Pre-requirements
- docker
- docker-compose
- maven (optionally)
## :hammer: Build images
```shell
sh build.sh
```
or manually build each project

```shell
mvn clean install -P docker
```
## :rocket: Running project
The easiest way to start project is to use `docker-compose.yml` where is configuration for services and ELK stack. 
```shell
docker-compose up
```

## Get Tasks Request
```shell
curl http://localhost:8080/task/1
```

## Todo List (In progress):

- [ ] Investigate how's sleuth work under the hood.
- [ ] Extend existing logs with fields like (operation, direction, time)
- [ ] Remove some fields in log from docker
- [ ] Create custom span
- [ ] Add additional service
- [ ] Prepare documentation and describe
