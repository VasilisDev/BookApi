# Use the official Maven image as the build image
FROM maven:3.8.3-openjdk-11-slim AS build

# Set the working directory to /app
WORKDIR /app

# Copy the project files to the container
COPY . .

# Build the application
RUN mvn clean package

# Use the official OpenJDK 11 image as the base image
FROM openjdk:11-jdk

# Set the working directory to /app
WORKDIR /app

# Copy the JAR file from the build image
COPY --from=build /app/target/book-1.0.0.jar book.jar

# Create a volume for MySQL data
VOLUME /var/lib/mysql

# Copy the database initialization script to the container
COPY db/init-db.sql /docker-entrypoint-initdb.d/

# Make port 8080 available to the world outside this container
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=docker", "book.jar"]