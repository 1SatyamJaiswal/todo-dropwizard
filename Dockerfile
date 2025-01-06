# Java 21 base image
FROM eclipse-temurin:21-jre-jammy

# Set working directory
WORKDIR /app

# Environment variables for MySQL connection
ENV DB_URL=jdbc:mysql://host.docker.internal/todolist

# Create the directory for JAR
RUN mkdir -p /app/target

# Copy JAR and config file
COPY target/*-SNAPSHOT.jar /app/target/application.jar
COPY config.yml /app/config.yml

# Expose Dropwizard ports
EXPOSE 8080 8081

# Command to run the application
CMD ["java", "-jar", "/app/target/application.jar", "server", "/app/config.yml"]