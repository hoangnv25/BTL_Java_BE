# ==========================
# Stage 1: Build Spring Boot JAR
# ==========================
FROM maven:3.9.8-amazoncorretto-21 AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src

RUN mvn package -DskipTests


# ==========================
# Stage 2: Run with Nginx + Java
# ==========================
FROM amazoncorretto:21.0.4

# Set working directory
WORKDIR /app

# Copy JAR từ stage build
COPY --from=build /app/target/*.jar app.jar

# Install Nginx
RUN yum install -y nginx && mkdir -p /run/nginx

# Copy Nginx config
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Expose port (Railway sẽ inject PORT thật)
ENV PORT=8080
EXPOSE 8080

# Start cả Nginx và Java cùng lúc
CMD service nginx start && java -jar app.jar
