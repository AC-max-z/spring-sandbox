FROM eclipse-temurin:21
RUN mkdir "/spring-boot-sandbox-app"
LABEL authors="mzamota"

COPY /build/libs/backend-0.0.1-SNAPSHOT.jar /spring-boot-sandbox-app/app.jar
WORKDIR /spring-boot-sandbox-app

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]