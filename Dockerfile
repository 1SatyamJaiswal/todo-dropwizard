# Use Eclipse Temurin as base image for Java 17
FROM eclipse-temurin:21-jre-jammy

# Set working directory
WORKDIR /app

# Environment variables for MySQL connection
ENV DB_URL=jdbc:mysql://host.docker.internal/todolist

# Create the directory for the fat JAR
RUN mkdir -p /app/target

# Copy the fat JAR and config file
COPY target/*-SNAPSHOT.jar /app/target/application.jar
COPY config.yml /app/config.yml

# Update the config file to use the environment variables
RUN sed -i 's|jdbc:mysql://localhost/todolist|'"${DB_URL}"'|g' /app/config.yml

# Expose Dropwizard ports
EXPOSE 8080 8081

# Command to run the application
CMD ["java", "-jar", "/app/target/application.jar", "server", "/app/config.yml"]