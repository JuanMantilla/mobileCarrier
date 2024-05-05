#Use Maven base image 
FROM maven:3.6.3-openjdk-17-slim AS builder
COPY src /home/app/src
COPY pom.xml /home/app
#Build an uber jar
RUN mvn -f /home/app/pom.xml package
WORKDIR /home/app/target
#Build the project
RUN mvn -f /home/app/pom.xml clean install -DskipTests 
#Extract the uber jar into layers
RUN java -Djarmode=layertools -jar /home/app/target/*.jar extract

FROM openjdk:22-ea-21-slim-bullseye

WORKDIR /opt/app

USER root
#Copy individual layers one by one 
COPY --from=builder /home/app/target/dependencies/ ./
#Add this to fix a bug which happens during sequential copy commands
RUN true
COPY --from=builder /home/app/target/spring-boot-loader/ ./
RUN true
COPY --from=builder /home/app/target/snapshot-dependencies/ ./
RUN true
COPY --from=builder /home/app/target/application/ ./

#Expose port on which Spring Boot app will run
EXPOSE 8080

#Switch to non root user for security
USER 1001

#Start Spring Boot app
ENTRYPOINT  ["java", "org.springframework.boot.loader.launch.JarLauncher"]