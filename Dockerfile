# ==============================
# 1️⃣ Build Stage
# ==============================
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app

# Copy Maven files first to leverage Docker cache
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Download dependencies (cached layer)
RUN ./mvnw dependency:go-offline -B

# Copy source code and build
COPY src ./src
RUN ./mvnw clean package -DskipTests

# ==============================
# 2️⃣ Runtime Stage
# ==============================
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy only the built jar from builder
COPY --from=builder /app/target/icem-backend.jar app.jar

# Expose the port (matches your application.properties)
EXPOSE 8080

# Set environment (optional but helpful)
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Run the Spring Boot application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
