# SFG Beer Works - RESTful Brewery Service

This project is to support learning about Restful APIs.

You can access the API documentation [here](https://sfg-beer-works.github.io/brewery-api/#tag/Beer-Service)

## Build project

with maven install a docker image is pushed to the docker repository with the image name local/kbe-rest-brewery:0.0.1-SNAPSHOT

### Docker Commands
#### Simple und directory docker
Build
``` 
docker build  -f ./docker/Dockerfile -t kbe-rest . 
```
Run
``` 
docker run -p 8080:8080 -d kbe-rest 
```

or without daeomon flag

``` 
docker run -p 8080:8080 kbe-rest 
```

check what is running
``` 
docker ps 
```
stop running container

``` 
docker stop [container-id] 
```

#### Layered und directory dockerLayered

needs maven configuration for spring-boot-maven-plugin

``` docker build  -f ./dockerLayered/Dockerfile -t kbe-rest . ```

### Kubernetes Command

Generate Kubernetes Deployment Yaml
```
kubectl create deployment kbe-rest-brewery --image=local/kbe-rest-brewery:0.0.1-SNAPSHOT --dry-run=client -o yaml > kbe-rest-brewery-deployment.yaml
```

Apply Deployment Yaml
```
kubectl apply -f kbe-rest-brewery-deployment.yaml
```

Check Deployment
```
kubectl get all
```

Generate Kubernetes Service Yaml
```
kubectl create service clusterip kbe-rest-brewery --tcp=8080:8080 --dry-run=client -o yaml > kbe-rest-brewery-service.yaml
```

Apply Service Yaml
```
kubectl apply -f kbe-rest-brewery-service.yaml
```

Check Service
```
kubectl get all
```

Expose Port 8080 and redirect traffic to our service in the internal network. This command is blocking. Ctl-C to terminate.
```
kubectl port-forward service/kbe-rest-brewery 8080:8080
```
Another and better approach is using the Ingress Controller. See gateway project how to do it.
A third way is to change the service and using the NodePort (```type: NodePort``` instead of ```type: ClusterIP```)
This expose a random port (get it with ```kubectl get all ```)

Stop and Remove Service and Deployment
```
kubectl delete service kbe-rest-brewery
kubectl delete deployment kbe-rest-brewery
```

Show logs
First get the Pod Id with ```kubectl get all ```

```
kubectl logs --tail=20 kbe-rest-brewery-6bd69bf9d8-4js4j
```
follow logs
```
kubectl logs -f kbe-rest-brewery-6bd69bf9d8-4js4j
```



