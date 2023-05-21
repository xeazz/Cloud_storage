FROM openjdk:17-jdk-slim-buster
EXPOSE 8080
ADD target/CloudStorage-0.0.1-SNAPSHOT.jar cloud_storage.jar
CMD ["java", "-jar", "/cloud_storage.jar"]