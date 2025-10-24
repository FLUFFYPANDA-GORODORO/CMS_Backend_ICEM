# ==============================
# 1️⃣ Build Stage
# ==============================
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app

# Copy Maven wrapper & POM first to leverage Docker cache
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# ✅ Grant execute permission to Maven Wrapper
RUN chmod +x mvnw

# Download dependencies (cached layer)
RUN ./mvnw dependency:go-offline -B

# Copy source code and build the JAR
COPY src ./src
RUN ./mvnw clean package -DskipTests

# ==============================
# 2️⃣ Runtime Stage
# ==============================
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy the built JAR from builder stage
COPY --from=builder /app/target/icem-backend.jar app.jar

# Expose the port
EXPOSE 8080

# Optional: Configure JVM memory limits for Render’s free instance
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Run the Spring Boot application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
