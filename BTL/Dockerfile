# Stage 1: Build
FROM maven:3.9.8-amazoncorretto-21 AS build

# Set working directory inside container
WORKDIR /app

# Copy source code and pom.xml into container
COPY BTL/pom.xml .
COPY BTL/src ./src

# Build source with Maven
RUN mvn clean package -DskipTests

# Stage 2: Runtime image
FROM amazoncorretto:21.0.4

# Set working directory
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose port (Railway auto maps this)
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
