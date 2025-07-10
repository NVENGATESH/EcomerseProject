# Use OpenJDK base image
FROM openjdk:17-jdk-alpine

# Set a path variable for the JAR file
ARG JAR_FILE=target/*.jar

# Copy the JAR file to the container
COPY ${JAR_FILE} app.jar

# Run the JAR file
ENTRYPOINT ["java", "-jar", "/app.jar"]
