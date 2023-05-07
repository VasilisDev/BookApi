# Use the official OpenJDK 11 image as the base image
FROM openjdk:11-jdk

# Set the working directory to /app
WORKDIR /app

# Copy the application jar file to the container
COPY target/book-1.0.0.jar book.jar


# Make port 8080 available to the world outside this container
EXPOSE 8080

# Set the entrypoint command to run the application
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=docker", "book.jar"]
