FROM mcr.microsoft.com/openjdk/jdk:21-ubuntu

LABEL authors="yogeshwar.vhatkar"

WORKDIR /app

COPY build/libs/trivydemo-0.0.1-SNAPSHOT.jar trivy-demo.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar","trivy-demo.jar"]