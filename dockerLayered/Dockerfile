FROM openjdk:21-slim as builder
WORKDIR application
ADD target/kbe-rest-brewery-0.0.1-SNAPSHOT.jar ./
RUN java -Djarmode=layertools -jar kbe-rest-brewery-0.0.1-SNAPSHOT.jar extract

FROM openjdk:21-slim

WORKDIR application
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/application/ ./
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "org.springframework.boot.loader.JarLauncher"]
