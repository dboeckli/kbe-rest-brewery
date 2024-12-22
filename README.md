# SFG Beer Works - RESTful Brewery Service

This project is to support learning about Restful APIs.

You can access the API documentation [here](https://sfg-beer-works.github.io/brewery-api/#tag/Beer-Service)

### Commands
#### Simple und directory docker

``` docker build  -f ./docker/Dockerfile -t kbe-rest . ```
``` docker run -p 8080:8080 -d kbe-rest ```
or without daeomon flag
``` docker run -p 8080:8080 kbe-rest ```
check what is running
``` docker ps ```
stop running container
``` docker stop [container-id] ```

#### Layered und directory dockerLayered

needs maven configuration for spring-boot-maven-plugin

``` docker build  -f ./dockerLayered/Dockerfile -t kbe-rest . ```



