FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY target/classes /app

COPY target/routers-test-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]