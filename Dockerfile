FROM eclipse-temurin:17-jdk-jammy

# Set the working directory inside the container
WORKDIR /app

# Create directory for the H2 database
RUN mkdir -p /app/data

# Copy the JAR file into the container
COPY target/welcome-page-0.0.1-SNAPSHOT.jar /app/welcome-page.jar

# Application runs on port 8181
EXPOSE 8181

# Run the application
ENTRYPOINT ["java", "-jar", "welcome-page.jar"]
