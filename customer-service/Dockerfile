#BUILD
FROM maven:3.9.6 AS builder

WORKDIR /opt/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN mvn clean package -DskipTests -X


#RUN
FROM openjdk:21-slim AS final

WORKDIR /opt/app
ARG JAR_FILE=/opt/app/target/customer-service-*SNAPSHOT.jar
COPY --from=builder ${JAR_FILE} customer-app.jar

ENTRYPOINT ["java", "-jar", "customer-app.jar"]
EXPOSE 8085